package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.SalesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesClient salesClient;

    @GetMapping
    public Map<String, Object> getSalesData() {
        Map<String, Object> response = new HashMap<>();
        response.put("orders", salesClient.getAllOrders());
        response.put("customers", salesClient.getAllCustomers());
        return response;
    }
}
