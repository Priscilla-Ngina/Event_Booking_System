package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.dto.OrganizerApplicationRequest;
import com.eventbooking.event_booking_system.dto.OrganizerApplicationResponse;
import com.eventbooking.event_booking_system.entity.OrganizerApplication;
import com.eventbooking.event_booking_system.entity.User;
import com.eventbooking.event_booking_system.repository.OrganizerApplicationRepository;
import com.eventbooking.event_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDateTime;

@Service
public class OrganizerApplicationService {

    private final OrganizerApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public OrganizerApplicationService(
            OrganizerApplicationRepository applicationRepository,
            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrganizerApplicationResponse submitApplication(
            String userEmail,
            OrganizerApplicationRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() != User.Role.CUSTOMER) {
            throw new IllegalStateException(
                    "Only customers can apply to become organizers");
        }

        OrganizerApplication application =
                applicationRepository.findByUserId(user.getId())
                        .orElse(null);

        if (application != null) {
            if (application.getStatus()
                    != OrganizerApplication.ApplicationStatus.REJECTED) {
                throw new IllegalStateException(
                        "You already have an active organizer application");
            }

            application.setBusinessName(request.getBusinessName());
            application.setBusinessDescription(
                    request.getBusinessDescription());
            application.setStatus(
                    OrganizerApplication.ApplicationStatus.PENDING);
            application.setSubmittedAt(LocalDateTime.now());
            application.setReviewedAt(null);

        } else {
            application = new OrganizerApplication();
            application.setUser(user);
            application.setBusinessName(request.getBusinessName());
            application.setBusinessDescription(
                    request.getBusinessDescription());
            application.setSubmittedAt(LocalDateTime.now());
        }

        OrganizerApplication saved =
                applicationRepository.save(application);

        return new OrganizerApplicationResponse(
                saved.getId(),
                saved.getBusinessName(),
                saved.getBusinessDescription(),
                saved.getStatus(),
                saved.getSubmittedAt(),
                saved.getReviewedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<OrganizerApplicationResponse> getPendingApplications() {

        return applicationRepository.findByStatus(
                        OrganizerApplication.ApplicationStatus.PENDING
                ).stream()
                .map(application -> new OrganizerApplicationResponse(
                        application.getId(),
                        application.getBusinessName(),
                        application.getBusinessDescription(),
                        application.getStatus(),
                        application.getSubmittedAt(),
                        application.getReviewedAt()
                ))
                .toList();
    }

    @Transactional
    public OrganizerApplicationResponse approveApplication(Long applicationId) {

        OrganizerApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Application not found"));

        if (application.getStatus()
                != OrganizerApplication.ApplicationStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending applications can be approved");
        }

        User user = application.getUser();
        user.setRole(User.Role.ORGANIZER);

        application.setStatus(
                OrganizerApplication.ApplicationStatus.APPROVED);
        application.setReviewedAt(LocalDateTime.now());

        return new OrganizerApplicationResponse(
                application.getId(),
                application.getBusinessName(),
                application.getBusinessDescription(),
                application.getStatus(),
                application.getSubmittedAt(),
                application.getReviewedAt()
        );
    }

    @Transactional
    public OrganizerApplicationResponse rejectApplication(Long applicationId) {

        OrganizerApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Application not found"));

        if (application.getStatus()
                != OrganizerApplication.ApplicationStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending applications can be rejected");
        }

        application.setStatus(
                OrganizerApplication.ApplicationStatus.REJECTED);
        application.setReviewedAt(LocalDateTime.now());

        return new OrganizerApplicationResponse(
                application.getId(),
                application.getBusinessName(),
                application.getBusinessDescription(),
                application.getStatus(),
                application.getSubmittedAt(),
                application.getReviewedAt()
        );
    }

}