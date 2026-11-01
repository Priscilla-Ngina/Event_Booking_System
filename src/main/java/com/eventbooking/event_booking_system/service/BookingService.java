package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.repository.BookingRepository;
import com.eventbooking.event_booking_system.repository.TicketTypeRepository;
import com.eventbooking.event_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.eventbooking.event_booking_system.dto.BookingRequest;
import com.eventbooking.event_booking_system.dto.BookingResponse;
import com.eventbooking.event_booking_system.dto.BookingItemResponse;
import com.eventbooking.event_booking_system.entity.Booking;
import com.eventbooking.event_booking_system.entity.BookingItem;
import com.eventbooking.event_booking_system.entity.Event;
import com.eventbooking.event_booking_system.entity.TicketType;
import com.eventbooking.event_booking_system.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          TicketTypeRepository ticketTypeRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(String customerEmail, BookingRequest request) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("A booking must contain at least one item");
        }

        Map<Long, BookingRequest.BookingItemRequest> requestedItems = new HashMap<>();
        Set<Long> ticketTypeIds = new HashSet<>();

        for (BookingRequest.BookingItemRequest item : request.getItems()) {
            if (item.getTicketTypeId() == null || item.getQuantity() == null
                    || item.getQuantity() < 1) {
                throw new IllegalArgumentException("Invalid ticket booking details");
            }

            if (!ticketTypeIds.add(item.getTicketTypeId())) {
                throw new IllegalArgumentException("A ticket type cannot appear more than once");
            }

            requestedItems.put(item.getTicketTypeId(), item);
        }

        List<Long> sortedIds = new ArrayList<>(ticketTypeIds);
        sortedIds.sort(Long::compareTo);

        List<TicketType> ticketTypes = new ArrayList<>();

        for (Long id : sortedIds) {
            TicketType ticketType = ticketTypeRepository.findByIdWithLock(id)
                    .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

            ticketTypes.add(ticketType);
        }

        Long eventId = ticketTypes.get(0).getEvent().getId();
        BigDecimal totalAmount = BigDecimal.ZERO;
        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setBookedAt(now);
        booking.setExpiresAt(now.plusMinutes(10));

        for (TicketType ticketType : ticketTypes) {

            BookingRequest.BookingItemRequest requestedItem =
                    requestedItems.get(ticketType.getId());

            if (!ticketType.getEvent().getId().equals(eventId)) {
                throw new IllegalArgumentException(
                        "All tickets in one booking must belong to the same event");
            }

            if (ticketType.getEvent().getStatus() != Event.EventStatus.PUBLISHED) {
                throw new IllegalStateException("The event is not available for booking");
            }

            if (!ticketType.getActive()) {
                throw new IllegalStateException("This ticket type is inactive");
            }

            if (ticketType.getSalesStartDate() == null
                    || ticketType.getSalesEndDate() == null
                    || now.isBefore(ticketType.getSalesStartDate())
                    || !now.isBefore(ticketType.getSalesEndDate())) {
                throw new IllegalStateException("Ticket sales are not currently open");
            }

            int quantity = requestedItem.getQuantity();

            if (ticketType.getAvailableQuantity() < quantity) {
                throw new IllegalStateException("Not enough tickets available");
            }

            BigDecimal unitPrice = ticketType.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            ticketType.setReservedQuantity(
                    ticketType.getReservedQuantity() + quantity);

            BookingItem bookingItem = new BookingItem();
            bookingItem.setTicketType(ticketType);
            bookingItem.setQuantity(quantity);
            bookingItem.setUnitPrice(unitPrice);
            bookingItem.setSubtotal(subtotal);

            booking.addBookingItem(bookingItem);

            totalAmount = totalAmount.add(subtotal);
        }

        booking.setTotalAmount(totalAmount);

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);


    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getCustomerBookings(String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        return bookingRepository.findByCustomer_Id(customer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getCustomerBooking(Long bookingId, String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Booking booking = bookingRepository
                .findByIdAndCustomer_Id(bookingId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Booking booking = bookingRepository
                .findByIdAndCustomer_IdForUpdate(bookingId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be cancelled");
        }

        List<Long> ticketTypeIds = booking.getBookingItems()
                .stream()
                .map(item -> item.getTicketType().getId())
                .distinct()
                .sorted()
                .toList();

        Map<Long, TicketType> lockedTicketTypes = new HashMap<>();

        for (Long ticketTypeId : ticketTypeIds) {
            TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

            lockedTicketTypes.put(ticketTypeId, ticketType);
        }

        for (BookingItem item : booking.getBookingItems()) {

            TicketType ticketType = lockedTicketTypes.get(
                    item.getTicketType().getId());

            if (ticketType.getReservedQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Invalid ticket reservation quantity");
            }

            ticketType.setReservedQuantity(
                    ticketType.getReservedQuantity() - item.getQuantity());
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {

        List<BookingItemResponse> itemResponses = booking.getBookingItems()
                .stream()
                .map(item -> new BookingItemResponse(
                        item.getTicketType().getId(),
                        item.getTicketType().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();

        return new BookingResponse(
                booking.getId(),
                booking.getBookedAt(),
                booking.getExpiresAt(),
                booking.getTotalAmount(),
                booking.getStatus(),
                itemResponses
        );
    }

    @Transactional
    public void expireBooking(Long bookingId) {
        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElse(null);

        if (booking == null ||
                booking.getStatus() != Booking.BookingStatus.PENDING ||
                booking.getExpiresAt().isAfter(LocalDateTime.now())) {
            return;
        }

        List<Long> ticketTypeIds = booking.getBookingItems().stream()
                .map(item -> item.getTicketType().getId())
                .distinct()
                .sorted()
                .toList();

        Map<Long, TicketType> lockedTicketTypes = new HashMap<>();

        for (Long ticketTypeId : ticketTypeIds) {
            TicketType ticketType = ticketTypeRepository
                    .findByIdWithLock(ticketTypeId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Ticket type not found: " + ticketTypeId
                    ));

            lockedTicketTypes.put(ticketTypeId, ticketType);
        }

        for (BookingItem item : booking.getBookingItems()) {
            TicketType ticketType = lockedTicketTypes.get(
                    item.getTicketType().getId()
            );

            if (ticketType.getReservedQuantity() < item.getQuantity()) {
                throw new IllegalStateException(
                        "Reserved ticket quantity is inconsistent"
                );
            }

            ticketType.setReservedQuantity(
                    ticketType.getReservedQuantity() - item.getQuantity()
            );
        }

        booking.setStatus(Booking.BookingStatus.EXPIRED);
    }

}
