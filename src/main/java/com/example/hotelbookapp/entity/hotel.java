package com.example.hotelbookapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;

    @Entity
    @Table(name = "hotel_details")
    @Data
    public class hotel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String HotelName;
        private String description;
        private String city;
        private String address;
        private String rating;
        private String imageUrl;
        private int price;
        private int totalRooms;
        private int availableRooms;
        // public void setRating(String i) {
        //     // TODO Auto-generated method stub
        //     throw new UnsupportedOperationException("Unimplemented method 'setRating'");
        // }
        // public void setPrice(int i) {
        //     // TODO Auto-generated method stub
        //     throw new UnsupportedOperationException("Unimplemented method 'setPrice'");
        // }
    }
