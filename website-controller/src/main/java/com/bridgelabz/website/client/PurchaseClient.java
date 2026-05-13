package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "purchase-service", url = "${services.purchase}")
public interface PurchaseClient {

    @GetMapping("/api/purchase-orders")
    List<Map<String, Object>> getAllOrders();

    @PostMapping("/api/purchase-orders")
    Map<String, Object> createOrder(@RequestBody Map<String, Object> order);

    @GetMapping("/api/purchase-orders/supplier/{supplierId}")
    List<Map<String, Object>> getOrdersBySupplier(@PathVariable("supplierId") Long supplierId);

    @PutMapping("/api/purchase-orders/{id}/approve")
    Map<String, Object> approveOrder(@PathVariable("id") Long id);

    @PutMapping("/api/purchase-orders/{id}/cancel")
    Map<String, Object> cancelOrder(@PathVariable("id") Long id);

    @PostMapping("/api/purchase-orders/{id}/receive")
    Map<String, Object> receiveGoods(@PathVariable("id") Long id, @RequestParam("warehouseId") Long warehouseId, @RequestBody Map<Long, Integer> receivedItems);
}
