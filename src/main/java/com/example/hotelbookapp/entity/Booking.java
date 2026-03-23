package com.example.hotelbookapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long hotelId;
    private Integer roomNumber;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // CONFIRMED or CANCELLED

    private Double totalAmount;
}