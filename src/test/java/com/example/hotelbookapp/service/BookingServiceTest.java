package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.entity.BookingStatus;
import com.example.hotelbookapp.repository.BookingRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setUserId(100L);
        booking.setHotelId(10L);
        booking.setRoomNumber(101);
        booking.setCheckInDate(LocalDate.of(2026, 3, 25));
        booking.setCheckOutDate(LocalDate.of(2026, 3, 28));
        booking.setStatus(BookingStatus.CONFIRMED);
    }

    //  Test: Booking conflict exists
    @Test
    void testCreateBooking_Conflict() {
        when(bookingRepo
            .findByHotelIdAndRoomNumberAndStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                anyLong(), anyInt(), any(), any(), any()))
            .thenReturn(Arrays.asList(new Booking())); // conflict exists

        String result = bookingService.createBooking(booking);

        assertEquals("Room is already booked for the selected dates!", result);
        verify(bookingRepo, never()).save(any());
    }

    //  Test: Successful booking
    @Test
    void testCreateBooking_Success() {
        when(bookingRepo
            .findByHotelIdAndRoomNumberAndStatusAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                anyLong(), anyInt(), any(), any(), any()))
            .thenReturn(Collections.emptyList()); // no conflict

        String result = bookingService.createBooking(booking);

        assertEquals("Booking confirmed!", result);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        verify(bookingRepo, times(1)).save(booking);
    }

    //  Test: Get booking history
    @Test
    void testGetBookingHistory() {
        List<Booking> mockList = Arrays.asList(booking);

        when(bookingRepo.findByUserId(100L)).thenReturn(mockList);

        List<Booking> result = bookingService.getBookingHistory(100L);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getUserId());
    }

    //  Test: Cancel booking - not found
    @Test
    void testCancelBooking_NotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());

        String result = bookingService.cancelBooking(1L);

        assertEquals("Booking not found!", result);
    }

    //  Test: Cancel booking - already cancelled
    @Test
    void testCancelBooking_AlreadyCancelled() {
        booking.setStatus(BookingStatus.CANCELLED);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        String result = bookingService.cancelBooking(1L);

        assertEquals("Booking is already cancelled!", result);
    }

    //  Test: Cancel booking - success
    @Test
    void testCancelBooking_Success() {
        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        String result = bookingService.cancelBooking(1L);

        assertEquals("Booking cancelled successfully!", result);
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepo, times(1)).save(booking);
    }
}