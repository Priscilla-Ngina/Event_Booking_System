package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>{

    List<Event> findByStatusOrderByStartDateAsc(Event.EventStatus status);

    List<Event> findByOrganizer_IdOrderByStartDateAsc(Long organizerId);

}
