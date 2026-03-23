package com.example.hotelbookapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.example.hotelbookapp.entity.hotel;
import com.example.hotelbookapp.service.HotelService;
import com.example.hotelbookapp.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("HotelBookApp/api/products")

public class HotelController {
    @Autowired
    private HotelService HotelService;
    @GetMapping("/allproducts")
    public List<hotel> getAllProducts() {
        return HotelService.getAllProducts();
    }

    @GetMapping("/viewproducts")
    public hotel viewProduct(@RequestParam("id") Long id) {
        return HotelService.getHotelById(id);
    }

    @GetMapping("/filterhotels")
    public hotel filterHotels(@RequestParam("price") Integer price,
            @RequestParam("HotelName") String HotelName,@RequestParam("City") String City) {
        return HotelService.getHotelByFilters(HotelName, City, price);
    }

    @PutMapping(value = "/updateProducts", consumes = "multipart/form-data")
    public hotel updateProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        hotel r = mapper.readValue(productJson, hotel.class);

        if (file != null && !file.isEmpty()) {
            String imageUrl = imageService.uploadImage(file);
            r.setImageUrl(imageUrl);
        }

        return HotelService.updateHotel(r.getId(), r);
    }

    @DeleteMapping("/deleteProducts")
    public String deleteProduct(@RequestParam("id") Long id) {
        hotel delprops = HotelService.deleteHotel(id);
        return "Product deleted successfully with id: " + id;
    }

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/saveProduct", consumes = "multipart/form-data")
    public hotel saveProduct(
            @RequestParam("Product") String productJson,
            @RequestParam("file") MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        hotel r = mapper.readValue(productJson, hotel.class);

        String imageUrl = imageService.uploadImage(file);
        r.setImageUrl(imageUrl);

        return HotelService.savedetails(r);
    }
}
