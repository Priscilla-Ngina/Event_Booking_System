package com.eventbooking.event_booking_system.repository;

import com.eventbooking.event_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer_Id(Long customerId);

    Optional<Booking> findByIdAndCustomer_Id(Long bookingId, Long customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId AND b.customer.id = :customerId")
    Optional<Booking> findByIdAndCustomer_IdForUpdate(
            @Param("bookingId") Long bookingId,
            @Param("customerId") Long customerId
    );


    List<Booking> findByStatusAndExpiresAtLessThanEqual(
            Booking.BookingStatus status,
            LocalDateTime now
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
    Optional<Booking> findByIdForUpdate(@Param("bookingId") Long bookingId);

}