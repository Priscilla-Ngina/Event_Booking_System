package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OrganizerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user-id", nullable=false)
    private User user;

    @Column(nullable=false)
    private String businessName;

    private String businessDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ApplicationStatus status= ApplicationStatus.PENDING;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
