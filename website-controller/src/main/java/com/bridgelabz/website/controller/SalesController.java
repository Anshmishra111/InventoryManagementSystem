package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.MovementClient;
import com.bridgelabz.website.client.ProductClient;
import com.bridgelabz.website.client.SalesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesClient salesClient;
    private final ProductClient productClient;
    private final MovementClient movementClient;

    @GetMapping("/orders")
    public List<Map<String, Object>> getOrders() {
        return salesClient.getAllOrders();
    }

    @PostMapping("/orders")
    public Map<String, Object> placeOrder(@RequestBody Map<String, Object> order) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", 1L);
        payload.put("customerName", order.getOrDefault("customerName", "Walk-in"));
        payload.put("customerEmail", order.getOrDefault("customerEmail", ""));
        payload.put("customerPhone", order.getOrDefault("customerPhone", ""));
        payload.put("productId", order.get("productId") != null ? Long.valueOf(order.get("productId").toString()) : null);
        payload.put("quantity", order.get("quantity") != null ? Integer.valueOf(order.get("quantity").toString()) : 1);
        payload.put("unitPrice", order.getOrDefault("unitPrice", 0.0));
        payload.put("warehouseId", order.get("warehouseId") != null ? Long.valueOf(order.get("warehouseId").toString()) : null);
        return salesClient.placeOrder(payload);
    }

    @PostMapping("/orders/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(salesClient.updateStatus(id, status));
    }

    @PostMapping("/orders/{id}/fulfill")
    public ResponseEntity<Map<String, Object>> fulfillOrder(
            @PathVariable Long id,
            @RequestParam Long warehouseId) {

        // 1. Mark as COMPLETED
        Map<String, Object> order = salesClient.updateStatus(id, "COMPLETED");

        // 2. Get productId and quantity directly from the order
        Object productIdObj = order.get("productId");
        Object quantityObj = order.get("quantity");
        if (productIdObj == null || quantityObj == null) {
            throw new RuntimeException("Sales Order missing productId or quantity.");
        }
        Long productId = Long.valueOf(productIdObj.toString());
        Integer quantity = Integer.valueOf(quantityObj.toString());

        // 3. Create a STOCK_OUT movement (auto-updates warehouse + product stock)
        Map<String, Object> movement = new HashMap<>();
        movement.put("productId", productId);
        movement.put("warehouseId", warehouseId);
        movement.put("quantity", quantity);
        movement.put("type", "OUT");
        movement.put("notes", "Fulfilled Sales Order #" + id);
        movement.put("reason", "Fulfilled Sales Order #" + id);
        movementClient.recordMovement(movement);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/customers")
    public List<Map<String, Object>> getCustomers() {
        return salesClient.getAllCustomers();
    }

    @PostMapping("/customers")
    public Map<String, Object> addCustomer(@RequestBody Map<String, Object> customer) {
        return salesClient.createCustomer(customer);
    }
}
