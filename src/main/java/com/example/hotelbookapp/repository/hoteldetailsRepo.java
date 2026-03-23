package com.example.hotelbookapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelbookapp.entity.hotel;

@Repository

public interface hoteldetailsRepo extends JpaRepository<hotel, Long> {
    List<hotel> findByrating(String rating);

    default hotel findByHotelNameAndCityAndPrice(
            String HotelName,
            String city,
            Integer price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByHotelNameAndCity'");
    }

    List<hotel> findByCity(String city);
}