package com.example.hotelbookapp.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentControllerTest {

    @Test
    void testCreateOrder_RealClient() throws Exception {
        PaymentController controller = new PaymentController();

        int amount = 500; // Rs. 500
        JSONObject response = controller.createOrder(amount);

        assertNotNull(response);
        assertEquals(amount * 100, response.getInt("amount"));
        assertEquals("INR", response.getString("currency"));
        assertTrue(response.has("id")); // Check that Razorpay order ID exists

        // Print order ID (optional)
        System.out.println("Razorpay order ID: " + response.getString("id"));
    }
}