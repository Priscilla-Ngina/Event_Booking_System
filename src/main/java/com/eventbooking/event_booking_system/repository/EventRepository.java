package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
