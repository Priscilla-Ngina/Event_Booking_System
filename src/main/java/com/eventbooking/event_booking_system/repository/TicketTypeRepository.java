package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
