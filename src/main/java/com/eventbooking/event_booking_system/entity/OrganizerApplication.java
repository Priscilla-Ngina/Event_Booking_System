package com.eventbooking.event_booking_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OrganizerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user-id", nullable=false, unique=true)
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

    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

}
