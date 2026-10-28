package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name= "booking_id", nullable=false)
    private Booking booking;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status= PaymentStatus.PENDING;

    private String providerReference;

    private LocalDateTime paidAt;

    public enum PaymentMethod {
        MPESA,
        CARD
    }

    public enum PaymentStatus {
        PENDING,
        SUCCESSFUL,
        FAILED,
    }
}
