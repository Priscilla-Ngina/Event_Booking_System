package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
