package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.entity.Booking;
import com.eventbooking.event_booking_system.repository.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public BookingExpiryScheduler(
            BookingRepository bookingRepository,
            BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    @Scheduled(fixedDelay = 30000)
    public void expirePendingBookings() {
        List<Booking> expiredBookings =
                bookingRepository.findByStatusAndExpiresAtLessThanEqual(
                        Booking.BookingStatus.PENDING,
                        LocalDateTime.now()
                );

        for (Booking booking : expiredBookings) {
            bookingService.expireBooking(booking.getId());
        }
    }
}