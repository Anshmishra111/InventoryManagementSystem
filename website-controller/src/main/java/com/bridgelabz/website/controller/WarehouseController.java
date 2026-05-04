package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseClient warehouseClient;

    @GetMapping
    public List<Map<String, Object>> getWarehouseData() {
        return warehouseClient.getAllWarehouses();
    }

    @PostMapping
    public Map<String, Object> addWarehouse(@RequestBody Map<String, Object> warehouse) {
        return warehouseClient.createWarehouse(warehouse);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateWarehouse(@PathVariable Long id, @RequestBody Map<String, Object> warehouse) {
        return warehouseClient.updateWarehouse(id, warehouse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseClient.deleteWarehouse(id);
        return ResponseEntity.ok().build();
    }
}
