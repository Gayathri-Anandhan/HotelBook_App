package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.entity.BookingStatus;
import com.example.hotelbookapp.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    // Create a booking after checking for date conflicts
    public String createBooking(Booking booking) {
        // Check for overlapping bookings
        List<Booking> conflicts = bookingRepo.findByHotelIdAndRoomNumberAndStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                booking.getHotelId(),
                booking.getRoomNumber(),
                BookingStatus.CONFIRMED,
                booking.getCheckOutDate(),
                booking.getCheckInDate()
        );

        if (!conflicts.isEmpty()) {
            return "Room is already booked for the selected dates!";
        }

        booking.setStatus(BookingStatus.CONFIRMED); // Set initial status
        bookingRepo.save(booking);
        return "Booking confirmed!";
    }

    // Get booking history for a user
    public List<Booking> getBookingHistory(Long userId) {
        return bookingRepo.findByUserId(userId);
    }

    // Cancel a booking
    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "Booking is already cancelled!";
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepo.save(booking);
        return "Booking cancelled successfully!";
    }
}

