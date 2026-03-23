package com.example.hotelbookapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import com.example.hotelbookapp.entity.hotel;
import com.example.hotelbookapp.service.HotelService;
import com.example.hotelbookapp.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("hotelbook/api/hotel/")

public class HotelController {
    @Autowired
    private HotelService HotelService;

    @GetMapping("/allhotels")
    public List<hotel> getAllHotels() {
        return HotelService.getAllHotels();
    }

    @GetMapping("/viewhotels")
    public hotel viewHotel(@RequestParam("id") Long id) {
        return HotelService.getHotelById(id);
    }

    @GetMapping("/filterhotels")
    public hotel filterHotels(@RequestParam("price") Integer price,
            @RequestParam("HotelName") String HotelName, @RequestParam("City") String City) {
        return HotelService.getHotelByFilters(HotelName, City, price);
    }

    @PutMapping(value = "/updateHotels", consumes = "multipart/form-data")
    public hotel updateHotel(
            @RequestParam("hotel") String hotelJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        hotel r = mapper.readValue(hotelJson, hotel.class);

        if (file != null && !file.isEmpty()) {
            String imageUrl = imageService.uploadImage(file);
            r.setImageUrl(imageUrl);
        }

        return HotelService.updateHotel(r.getId(), r);
    }

    @DeleteMapping("/deleteHotels")
    public String deleteHotel(@RequestParam("id") Long id) {
        hotel delhotel = HotelService.deleteHotel(id);
        return "Hotel deleted successfully with id: " + id;
    }

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/saveHotel", consumes = "multipart/form-data")
    public hotel Hotel(
            @RequestParam("hotel") String hotelJson,
            @RequestParam("file") MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        hotel r = mapper.readValue(hotelJson, hotel.class);

        String imageUrl = imageService.uploadImage(file);
        r.setImageUrl(imageUrl);

        return HotelService.savedetails(r);
    }

    @Autowired
    private HotelService hotelService;

    @GetMapping("/search")
    public List<hotel> searchHotels(
            @RequestParam("city") String city,
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        return hotelService.searchHotels(city, checkIn, checkOut);
    }
}
