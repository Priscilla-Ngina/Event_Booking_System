package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
