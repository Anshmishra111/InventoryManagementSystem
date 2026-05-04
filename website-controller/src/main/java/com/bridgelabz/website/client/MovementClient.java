package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "movement-service", url = "${services.movement}")
public interface MovementClient {

    @PostMapping("/api/movements")
    Map<String, Object> recordMovement(@RequestBody Map<String, Object> movement);

    @GetMapping("/api/movements/inventory/warehouse/{warehouseId}")
    List<Map<String, Object>> getInventoryByWarehouseId(@PathVariable("warehouseId") Long warehouseId);

    @GetMapping("/api/movements/inventory/product/{productId}")
    List<Map<String, Object>> getInventoryByProductId(@PathVariable("productId") Long productId);

    @GetMapping("/api/movements/history/product/{productId}")
    List<Map<String, Object>> getMovementHistory(@PathVariable("productId") Long productId);

    @GetMapping("/api/movements")
    List<Map<String, Object>> getAllMovements();

    @GetMapping("/api/movements/inventory")
    List<Map<String, Object>> getAllInventory();

    @GetMapping("/api/movements/type/{type}")
    List<Map<String, Object>> getMovementsByType(@PathVariable("type") String type);

    @GetMapping("/api/movements/reference")
    List<Map<String, Object>> getMovementsByReference(@RequestParam("id") Long id, @RequestParam("type") String type);

    @GetMapping("/api/movements/performer")
    List<Map<String, Object>> getMovementsByPerformedBy(@RequestParam("email") String email);
}
