package com.example.hotelbookapp.controller;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setUserId(1L);

        when(bookingService.createBooking(booking)).thenReturn("Booking confirmed!");

        ResponseEntity<String> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());          //  use HttpStatus
        assertEquals("Booking confirmed!", response.getBody());
        verify(bookingService, times(1)).createBooking(booking);
    }

    @Test
    void testCreateBooking_Failure() {
        Booking booking = new Booking();
        booking.setUserId(1L);

        when(bookingService.createBooking(booking)).thenReturn("Booking failed!");

        ResponseEntity<String> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  //  use HttpStatus
        assertEquals("Booking failed!", response.getBody());
        verify(bookingService, times(1)).createBooking(booking);
    }

    @Test
    void testGetBookingHistory() {
        Long userId = 1L;
        Booking b1 = new Booking();
        b1.setUserId(userId);
        Booking b2 = new Booking();
        b2.setUserId(userId);

        List<Booking> bookings = Arrays.asList(b1, b2);

        when(bookingService.getBookingHistory(userId)).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getBookingHistory(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());          //  use HttpStatus
        assertEquals(2, response.getBody().size());
        verify(bookingService, times(1)).getBookingHistory(userId);
    }

    @Test
    void testCancelBooking_Success() {
        Long bookingId = 1L;

        when(bookingService.cancelBooking(bookingId)).thenReturn("Booking cancelled successfully!");

        ResponseEntity<String> response = bookingController.cancelBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());          //  already correct
        assertEquals("Booking cancelled successfully!", response.getBody());
        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    @Test
    void testCancelBooking_Failure() {
        Long bookingId = 2L;

        when(bookingService.cancelBooking(bookingId)).thenReturn("Booking not found!");

        ResponseEntity<String> response = bookingController.cancelBooking(bookingId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  //  use HttpStatus
        assertEquals("Booking not found!", response.getBody());
        verify(bookingService, times(1)).cancelBooking(bookingId);
    }
}