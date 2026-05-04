package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthClient authClient;
    private final WarehouseClient warehouseClient;
    private final AlertClient alertClient;
    private final ReportClient reportClient;
    private final MovementClient movementClient;

    // --- User Management ---
    @GetMapping("/users")
    public List<Map<String, Object>> manageUsers() {
        return authClient.getAllUsers();
    }

    @PostMapping("/users")
    public Map<String, Object> addUser(@RequestBody Map<String, Object> userData) {
        return authClient.register(userData);
    }

    @PutMapping("/users/{id}")
    public Map<String, Object> editUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        return authClient.updateUser(id, userData);
    }

    @DeleteMapping("/users/{id}")
    public void deactivateUser(@PathVariable Long id) {
        authClient.deleteUser(id);
    }

    // --- Warehouse Management ---
    @GetMapping("/warehouses")
    public List<Map<String, Object>> manageWarehouses() {
        return warehouseClient.getAllWarehouses();
    }

    @PostMapping("/warehouses")
    public Map<String, Object> addWarehouse(@RequestBody Map<String, Object> warehouse) {
        return warehouseClient.createWarehouse(warehouse);
    }

    @PutMapping("/warehouses/{id}")
    public Map<String, Object> editWarehouse(@PathVariable Long id, @RequestBody Map<String, Object> warehouse) {
        return warehouseClient.updateWarehouse(id, warehouse);
    }

    @DeleteMapping("/warehouses/{id}")
    public void deactivateWarehouse(@PathVariable Long id) {
        warehouseClient.deleteWarehouse(id);
    }

    // --- System Analytics & Audit ---
    @GetMapping("/reports/stock-value")
    public List<Map<String, Object>> viewTotalStockValue() {
        return reportClient.getInventoryHealth(); // Placeholder for value report
    }

    @GetMapping("/audit-logs")
    public List<Map<String, Object>> viewAuditLogs() {
        return movementClient.getAllMovements(); // Movements serve as the audit trail
    }

    @PostMapping("/platform-alert")
    public void sendPlatformAlert(@RequestBody Map<String, Object> alertData) {
        // Implementation for system-wide alerts
    }
}
