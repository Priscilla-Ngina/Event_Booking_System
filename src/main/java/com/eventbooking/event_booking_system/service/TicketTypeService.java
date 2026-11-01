package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.dto.TicketTypeRequest;
import com.eventbooking.event_booking_system.dto.TicketTypeResponse;
import com.eventbooking.event_booking_system.entity.Event;
import com.eventbooking.event_booking_system.entity.TicketType;
import com.eventbooking.event_booking_system.entity.User;
import com.eventbooking.event_booking_system.repository.EventRepository;
import com.eventbooking.event_booking_system.repository.TicketTypeRepository;
import com.eventbooking.event_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketTypeService(
            TicketTypeRepository ticketTypeRepository,
            EventRepository eventRepository,
            UserRepository userRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketTypeResponse createTicketType(
            Long eventId, String organizerEmail, TicketTypeRequest request) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException(
                    "You cannot manage ticket types for another organizer's event");
        }

        if (event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot create ticket types for a cancelled event");
        }

        if (!request.getSalesEndDate().isAfter(request.getSalesStartDate())) {
            throw new IllegalArgumentException(
                    "Sales end date must be after sales start date");
        }

        if (request.getSalesEndDate().isAfter(event.getStartDate())) {
            throw new IllegalArgumentException(
                    "Ticket sales must end before the event starts");
        }

        TicketType ticketType = new TicketType();
        ticketType.setEvent(event);
        ticketType.setName(request.getName());
        ticketType.setPrice(request.getPrice());
        ticketType.setTotalQuantity(request.getTotalQuantity());
        ticketType.setSoldQuantity(0);
        ticketType.setSalesStartDate(request.getSalesStartDate());
        ticketType.setSalesEndDate(request.getSalesEndDate());
        ticketType.setActive(true);

        TicketType saved = ticketTypeRepository.save(ticketType);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketTypeResponse> getAvailableTicketTypes(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (event.getStatus() != Event.EventStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Ticket options are only available for published events");
        }

        LocalDateTime now = LocalDateTime.now();

        return ticketTypeRepository.findByEvent_IdAndActiveTrue(eventId)
                .stream()
                .filter(ticketType ->
                        ticketType.getSalesStartDate().isBefore(now)
                                || ticketType.getSalesStartDate().isEqual(now))
                .filter(ticketType ->
                        ticketType.getSalesEndDate().isAfter(now))
                .filter(ticketType ->
                        ticketType.getSoldQuantity() < ticketType.getTotalQuantity())
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public TicketTypeResponse updateTicketType(
            Long ticketTypeId,
            String organizerEmail,
            TicketTypeRequest request) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

        Event event = ticketType.getEvent();

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException(
                    "You cannot update another organizer's ticket type");
        }

        if (event.getStatus() == Event.EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot update ticket types for a cancelled event");
        }

        if (request.getTotalQuantity() < ticketType.getSoldQuantity()) {
            throw new IllegalArgumentException(
                    "Total quantity cannot be less than tickets already sold");
        }

        if (!request.getSalesEndDate().isAfter(request.getSalesStartDate())) {
            throw new IllegalArgumentException(
                    "Sales end date must be after sales start date");
        }

        if (request.getSalesEndDate().isAfter(event.getStartDate())) {
            throw new IllegalArgumentException(
                    "Ticket sales must end before the event starts");
        }

        ticketType.setName(request.getName());
        ticketType.setPrice(request.getPrice());
        ticketType.setTotalQuantity(request.getTotalQuantity());
        ticketType.setSalesStartDate(request.getSalesStartDate());
        ticketType.setSalesEndDate(request.getSalesEndDate());

        return mapToResponse(ticketType);
    }

    @Transactional
    public TicketTypeResponse updateTicketTypeStatus(
            Long ticketTypeId,
            String organizerEmail,
            Boolean active) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

        if (!ticketType.getEvent().getOrganizer().getId()
                .equals(organizer.getId())) {
            throw new IllegalStateException(
                    "You cannot manage another organizer's ticket type");
        }

        if (ticketType.getEvent().getStatus() == Event.EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot change ticket types for a cancelled event");
        }

        ticketType.setActive(active);

        return mapToResponse(ticketType);
    }

    @Transactional(readOnly = true)
    public List<TicketTypeResponse> getOrganizerTicketTypes(
            Long eventId,
            String organizerEmail) {

        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new IllegalStateException(
                    "You cannot manage ticket types for another organizer's event");
        }

        return ticketTypeRepository.findByEvent_Id(eventId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TicketTypeResponse mapToResponse(TicketType ticketType) {
        return new TicketTypeResponse(
                ticketType.getId(),
                ticketType.getEvent().getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getTotalQuantity(),
                ticketType.getSoldQuantity(),
                ticketType.getReservedQuantity(),
                ticketType.getSalesStartDate(),
                ticketType.getSalesEndDate(),
                ticketType.getActive()
        );
    }
}