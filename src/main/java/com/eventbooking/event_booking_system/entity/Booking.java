package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private User customer;

    @Column(nullable=false)
    private LocalDateTime bookedAt;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status= BookingStatus.PENDING;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
    }
}
