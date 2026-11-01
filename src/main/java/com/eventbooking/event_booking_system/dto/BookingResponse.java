package com.eventbooking.event_booking_system.dto;

import com.eventbooking.event_booking_system.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private LocalDateTime bookedAt;
    private LocalDateTime expiresAt;
    private BigDecimal totalAmount;
    private Booking.BookingStatus status;
    private List<BookingItemResponse> items;

    public BookingResponse(Long id, LocalDateTime bookedAt, LocalDateTime expiresAt,
                           BigDecimal totalAmount,
                           Booking.BookingStatus status,
                           List<BookingItemResponse> items) {
        this.id = id;
        this.bookedAt = bookedAt;
        this.expiresAt = expiresAt;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public Long getId() { return id; }
    public LocalDateTime getBookedAt() { return bookedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Booking.BookingStatus getStatus() { return status; }
    public List<BookingItemResponse> getItems() { return items; }


}