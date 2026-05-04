package com.bridgelabz.website.client;

import com.bridgelabz.website.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "sales-service", url = "${services.sales}", configuration = FeignConfig.class)
public interface SalesClient {

    @GetMapping("/api/sales/orders")
    List<Map<String, Object>> getAllOrders();

    @PostMapping("/api/sales/orders")
    Map<String, Object> placeOrder(@RequestBody Map<String, Object> order);

    @GetMapping("/api/sales/customers")
    List<Map<String, Object>> getAllCustomers();

    @PostMapping("/api/sales/customers")
    Map<String, Object> createCustomer(@RequestBody Map<String, Object> customer);

    @PutMapping("/api/sales/orders/{id}/status")
    Map<String, Object> updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}
