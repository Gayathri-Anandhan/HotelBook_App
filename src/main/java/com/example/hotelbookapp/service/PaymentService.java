package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.Booking;
import com.example.hotelbookapp.entity.BookingStatus;
import com.example.hotelbookapp.entity.Payment;
import com.example.hotelbookapp.entity.PaymentStatus;
import com.example.hotelbookapp.repository.BookingRepo;
import com.example.hotelbookapp.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private BookingRepo bookingRepo;

    // Process payment for a booking
    public String processPayment(Long bookingId, Double amount) {
        Booking booking = bookingRepo.findById(bookingId).orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }

        // Simulate payment (always success here; can integrate real payment gateway)
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDate.now());

        paymentRepo.save(payment);

        // Confirm booking after payment
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepo.save(booking);

        return "Payment successful and booking confirmed!";
    }
}

