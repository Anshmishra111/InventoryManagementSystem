package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "warehouse-service", url = "${services.warehouse}")
public interface WarehouseClient {

    @GetMapping("/api/warehouses")
    List<Map<String, Object>> getAllWarehouses();

    @GetMapping("/api/warehouses/{id}")
    Map<String, Object> getWarehouseById(@PathVariable("id") Long id);

    @PostMapping("/api/warehouses")
    Map<String, Object> createWarehouse(@RequestBody Map<String, Object> warehouse);

    @PutMapping("/api/warehouses/{id}")
    Map<String, Object> updateWarehouse(@PathVariable("id") Long id, @RequestBody Map<String, Object> warehouse);

    @DeleteMapping("/api/warehouses/{id}")
    void deleteWarehouse(@PathVariable("id") Long id);
}
