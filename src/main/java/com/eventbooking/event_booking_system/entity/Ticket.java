package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "booking_id", nullable=false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name="ticket_type_id", nullable=false)
    private TicketType ticketType;

    private String seatNumber;

    @Column(nullable=false, unique=true)
    private String qrCodeToken;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status= TicketStatus.VALID;

    public enum TicketStatus {
        VALID,
        CANCELLED,
        USED
    }

}
