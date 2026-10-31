package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.OrganizerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrganizerApplicationRepository extends JpaRepository<OrganizerApplication,Long> {

//helps prevent duplicate applications
    Optional<OrganizerApplication> findByUserId(Long userId);

    //Allows admins to retrieve applications by status
    List<OrganizerApplication> findByStatus(
            OrganizerApplication.ApplicationStatus status);

}
