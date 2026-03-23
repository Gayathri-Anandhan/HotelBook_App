package com.example.hotelbookapp.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId; // Link to the booking
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // SUCCESS or FAILED

    private LocalDate paymentDate;
}

