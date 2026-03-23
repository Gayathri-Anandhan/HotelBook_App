package com.example.hotelbookapp.controller;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hotelbook/api/bookings")
@CrossOrigin(origins = "*") // allow frontend calls
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create a booking
    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        String result = bookingService.createBooking(booking);
        if (result.equals("Booking confirmed!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Get booking history for a user
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Booking>> getBookingHistory(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingHistory(userId);
        return ResponseEntity.ok(bookings);
    }

    //  Cancel a booking
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        String result = bookingService.cancelBooking(bookingId);
        if (result.equals("Booking cancelled successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}