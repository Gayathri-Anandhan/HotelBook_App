package com.example.hotelbookapp.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotelbook/api/payments")
@CrossOrigin("*")
public class PaymentController {

    private static final String KEY = "rzp_test_SP6YnALebZmwaG";
    private static final String SECRET = "PpdRc1OV8D67QtEBIAsOuX9Q";

    @PostMapping("/create-order")
    public JSONObject createOrder(@RequestParam int amount) throws Exception {

        RazorpayClient client = new RazorpayClient(KEY, SECRET);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_11");

        Order order = client.orders.create(orderRequest);

        return order.toJson();
    }
}