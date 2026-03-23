package com.example.hotelbookapp.service;

import com.cloudinary.utils.ObjectUtils;
import com.example.hotelbookapp.entity.hotel;
import com.example.hotelbookapp.repository.hoteldetailsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import com.cloudinary.Cloudinary;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelService {
    @Autowired
    private hoteldetailsRepo hotelDetailsRepo;

    public hotel savedetails(hotel e) {
        return hotelDetailsRepo.save(e);
    }

    public List<hotel> getAllHotels() {
        return hotelDetailsRepo.findAll();
    }

    public hotel getHotelById(Long id) {
        return hotelDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
    }

    public hotel getHotelByFilters(String HotelName, String City, Integer price) {
        return hotelDetailsRepo.findByHotelNameAndCityAndPrice(HotelName, City, price);
    }

    public hotel updateHotel(Long id, hotel updateProps) {
        hotel exisHotel = hotelDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel with this id: " + id + "is not found"));
        exisHotel.setHotelName(updateProps.getHotelName());
        exisHotel.setDescription(updateProps.getDescription());
        exisHotel.setCity(updateProps.getCity());
        exisHotel.setAddress(updateProps.getAddress());
        exisHotel.setRating(updateProps.getRating());
        exisHotel.setImageUrl(updateProps.getImageUrl());
        try {
            return hotelDetailsRepo.save(exisHotel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exisHotel;
    }

    public hotel deleteHotel(Long id) {
        hotel exisHotel = hotelDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotelDetailsRepo.deleteById(id);
        return exisHotel;
    }
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws Exception {

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }

    public List<hotel> searchHotelsByCity(String city) {
        return hotelDetailsRepo.findByCity(city);
    }

    public List<hotel> searchHotels(String city, LocalDate checkIn, LocalDate checkOut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchHotels'");
    }
}