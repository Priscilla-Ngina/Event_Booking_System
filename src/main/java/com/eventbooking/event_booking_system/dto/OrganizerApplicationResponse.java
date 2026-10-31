package com.eventbooking.event_booking_system.dto;

import com.eventbooking.event_booking_system.entity.OrganizerApplication;
import java.time.LocalDateTime;

public class OrganizerApplicationResponse {

    private final Long id;
    private final String businessName;
    private final String businessDescription;
    private final OrganizerApplication.ApplicationStatus status;
    private final LocalDateTime submittedAt;
    private final LocalDateTime reviewedAt;

    public OrganizerApplicationResponse(
            Long id,
            String businessName,
            String businessDescription,
            OrganizerApplication.ApplicationStatus status,
            LocalDateTime submittedAt,
            LocalDateTime reviewedAt) {

        this.id = id;
        this.businessName = businessName;
        this.businessDescription = businessDescription;
        this.status = status;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
    }

    public Long getId() { return id; }
    public String getBusinessName() { return businessName; }
    public String getBusinessDescription() { return businessDescription; }
    public OrganizerApplication.ApplicationStatus getStatus() { return status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
}