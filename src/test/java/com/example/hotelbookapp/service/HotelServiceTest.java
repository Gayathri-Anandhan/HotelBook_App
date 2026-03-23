package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.hotel;
import com.example.hotelbookapp.repository.hoteldetailsRepo;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private hoteldetailsRepo hotelDetailsRepo;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private HotelService hotelService;

    private hotel h;

    @BeforeEach
    void setUp() {
        h = new hotel();
        h.setId(1L);
        h.setHotelName("Test Hotel");
        h.setCity("Chennai");
        h.setPrice(2000);
    }

    //  Test: Save hotel
    @Test
    void testSaveDetails() {
        when(hotelDetailsRepo.save(h)).thenReturn(h);

        hotel result = hotelService.savedetails(h);

        assertEquals("Test Hotel", result.getHotelName());
    }

    //  Test: Get all hotels
    @Test
    void testGetAllProducts() {
        when(hotelDetailsRepo.findAll()).thenReturn(Arrays.asList(h));

        List<hotel> list = hotelService.getAllProducts();

        assertEquals(1, list.size());
    }

    //  Test: Get hotel by ID - success
    @Test
    void testGetHotelById_Success() {
        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.of(h));

        hotel result = hotelService.getHotelById(1L);

        assertEquals("Test Hotel", result.getHotelName());
    }

    //  Test: Get hotel by ID - not found
    @Test
    void testGetHotelById_NotFound() {
        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            hotelService.getHotelById(1L);
        });

        assertEquals("Hotel not found", ex.getMessage());
    }

    //  Test: Filter search
    @Test
    void testGetHotelByFilters() {
        when(hotelDetailsRepo.findByHotelNameAndCityAndPrice("Test Hotel", "Chennai", 2000))
                .thenReturn(h);

        hotel result = hotelService.getHotelByFilters("Test Hotel", "Chennai", 2000);

        assertEquals("Chennai", result.getCity());
    }

    //  Test: Update hotel
    @Test
    void testUpdateHotel() {
        hotel updated = new hotel();
        updated.setHotelName("Updated Hotel");
        updated.setCity("Madurai");

        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.of(h));
        when(hotelDetailsRepo.save(any())).thenReturn(h);

        hotel result = hotelService.updateHotel(1L, updated);

        assertEquals("Updated Hotel", result.getHotelName());
    }

    //  Test: Update hotel - not found
    @Test
    void testUpdateHotel_NotFound() {
        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            hotelService.updateHotel(1L, new hotel());
        });
    }

    //  Test: Delete hotel
    @Test
    void testDeleteHotel() {
        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.of(h));

        hotel result = hotelService.deleteHotel(1L);

        assertEquals(1L, result.getId());
        verify(hotelDetailsRepo, times(1)).deleteById(1L);
    }

    //  Test: Delete hotel - not found
    @Test
    void testDeleteHotel_NotFound() {
        when(hotelDetailsRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            hotelService.deleteHotel(1L);
        });
    }

    //  Test: Search by city
    @Test
    void testSearchHotelsByCity() {
        when(hotelDetailsRepo.findByCity("Chennai")).thenReturn(Arrays.asList(h));

        List<hotel> result = hotelService.searchHotelsByCity("Chennai");

        assertEquals(1, result.size());
    }
}