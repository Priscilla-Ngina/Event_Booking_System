package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
