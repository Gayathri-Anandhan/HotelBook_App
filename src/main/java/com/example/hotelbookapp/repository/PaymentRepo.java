package com.example.hotelbookapp.repository;

import com.example.hotelbookapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingId(Long bookingId);
}
