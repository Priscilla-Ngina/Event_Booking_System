package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="organizer_id", nullable=false)
    private User organizer;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable=false)
    private String venue;

    @Column(nullable=false)
    private LocalDateTime startDate;

    @Column(nullable=false)
    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private EventStatus status= EventStatus.DRAFT;

    public enum EventStatus {
        DRAFT,
        PUBLISHED,
        CANCELLED,

    }
}
