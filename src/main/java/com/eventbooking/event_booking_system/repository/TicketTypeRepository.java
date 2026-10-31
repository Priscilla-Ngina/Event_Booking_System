package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByEvent_Id(Long eventId);

    List<TicketType> findByEvent_IdAndActiveTrue(Long eventId);
}