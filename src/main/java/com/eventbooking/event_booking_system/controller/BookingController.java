package com.eventbooking.event_booking_system.controller;

import com.eventbooking.event_booking_system.dto.BookingRequest;
import com.eventbooking.event_booking_system.dto.BookingResponse;
import com.eventbooking.event_booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponse createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        return bookingService.createBooking(
                authentication.getName(),
                request
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingResponse> getCustomerBookings(Authentication authentication) {
        return bookingService.getCustomerBookings(authentication.getName());
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponse getCustomerBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {

        return bookingService.getCustomerBooking(
                bookingId,
                authentication.getName()
        );
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponse cancelBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {

        return bookingService.cancelBooking(
                bookingId,
                authentication.getName()
        );
    }

}