package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.entity.BookingStatus;
import com.example.hotelbookapp.entity.Payment;
import com.example.hotelbookapp.entity.PaymentStatus;
import com.example.hotelbookapp.repository.BookingRepo;
import com.example.hotelbookapp.repository.PaymentRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private PaymentService paymentService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.CONFIRMED);
    }

    //  Test: Booking not found
    @Test
    void testProcessPayment_BookingNotFound() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());

        String result = paymentService.processPayment(1L, 2000.0);

        assertEquals("Booking not found!", result);
        verify(paymentRepo, never()).save(any());
    }

    //  Test: Successful payment
    @Test
    void testProcessPayment_Success() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        String result = paymentService.processPayment(1L, 2000.0);

        assertEquals("Payment successful and booking confirmed!", result);

        // Verify payment saved
        verify(paymentRepo, times(1)).save(any(Payment.class));

        // Verify booking updated
        verify(bookingRepo, times(1)).save(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}