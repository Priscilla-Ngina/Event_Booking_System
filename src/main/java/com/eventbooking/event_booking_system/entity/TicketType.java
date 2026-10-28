package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal price;

    @Column(nullable=false)
    private Integer totalQuantity;
}
