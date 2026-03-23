package com.example.hotelbookapp.service;

import com.cloudinary.utils.ObjectUtils;
import com.example.hotelbookapp.entity.hotel;
import com.example.hotelbookapp.repository.hoteldetailsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import com.cloudinary.Cloudinary;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private hoteldetailsRepo hotelDetailsRepo;

    public hotel savedetails(hotel e) {
        return hotelDetailsRepo.save(e);
    }

    public List<hotel> getAllProducts() {
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
        hotel exisProduct = hotelDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel with this id: " + id + "is not found"));
        exisProduct.setHotelName(updateProps.getHotelName());
        exisProduct.setDescription(updateProps.getDescription());
        exisProduct.setCity(updateProps.getCity());
        exisProduct.setAddress(updateProps.getAddress());
        exisProduct.setRating(updateProps.getRating());
        exisProduct.setImageUrl(updateProps.getImageUrl());
        try {
            return hotelDetailsRepo.save(exisProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exisProduct;
    }

    public hotel deleteHotel(Long id) {
        hotel exisProduct = hotelDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        hotelDetailsRepo.deleteById(id);
        return exisProduct;
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
}