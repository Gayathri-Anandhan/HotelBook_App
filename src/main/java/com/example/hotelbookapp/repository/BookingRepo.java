package com.example.hotelbookapp.repository;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {

    // Get all bookings for a specific user
    List<Booking> findByUserId(Long userId);

    // Get all bookings for a hotel and room with a specific status
    List<Booking> findByHotelIdAndRoomNumberAndStatus(Long hotelId, Integer roomNumber, BookingStatus status);

    // Optional: Get bookings in a date range (useful for search conflicts)
    List<Booking> findByHotelIdAndRoomNumberAndStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            Long hotelId,
            Integer roomNumber,
            BookingStatus status,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );
}
