package com.eventbooking.event_booking_system.controller;

import com.eventbooking.event_booking_system.dto.OrganizerApplicationRequest;
import com.eventbooking.event_booking_system.dto.OrganizerApplicationResponse;
import com.eventbooking.event_booking_system.service.OrganizerApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organizer-applications")
public class OrganizerApplicationController {

    private final OrganizerApplicationService applicationService;

    public OrganizerApplicationController(
            OrganizerApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrganizerApplicationResponse submitApplication(
            @Valid @RequestBody OrganizerApplicationRequest request,
            Authentication authentication) {

        return applicationService.submitApplication(
                authentication.getName(),
                request
        );
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrganizerApplicationResponse> getPendingApplications() {
        return applicationService.getPendingApplications();
    }

    @PutMapping("/{applicationId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public OrganizerApplicationResponse approveApplication(
            @PathVariable Long applicationId) {

        return applicationService.approveApplication(applicationId);
    }

    @PutMapping("/{applicationId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public OrganizerApplicationResponse rejectApplication(
            @PathVariable Long applicationId) {

        return applicationService.rejectApplication(applicationId);
    }

}