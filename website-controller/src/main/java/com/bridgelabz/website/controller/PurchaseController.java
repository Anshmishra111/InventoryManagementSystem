package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseClient purchaseClient;
    private final SupplierClient supplierClient;

    // --- Purchase Orders ---
    @GetMapping("/orders")
    public List<Map<String, Object>> viewPurchaseOrders() {
        return purchaseClient.getAllOrders();
    }

    @PostMapping("/orders")
    public Map<String, Object> createPO(@RequestBody Map<String, Object> order) {
        System.out.println("DEBUG [website-controller]: Creating PO with payload: " + order);
        return purchaseClient.createOrder(order);
    }

    @PostMapping("/orders/{id}/status")
    public Map<String, Object> updatePOStatus(@PathVariable Long id, @RequestParam String status) {
        if ("APPROVED".equalsIgnoreCase(status)) {
            return purchaseClient.approveOrder(id);
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            return purchaseClient.cancelOrder(id);
        }
        throw new RuntimeException("Unsupported status: " + status);
    }

    @PostMapping("/orders/{id}/receive")
    public Map<String, Object> receiveGoods(
            @PathVariable Long id, 
            @RequestParam(required = false) Long warehouseId,
            @RequestBody(required = false) Map<Long, Integer> receivedItems) {
        
        Map<Long, Integer> itemsToProcess = receivedItems;
        if (itemsToProcess == null || itemsToProcess.isEmpty()) {
            itemsToProcess = new HashMap<>();
        }
        
        return purchaseClient.receiveGoods(id, warehouseId, itemsToProcess);
    }

    @GetMapping("/orders/supplier/{supplierId}")
    public List<Map<String, Object>> viewPOsBySupplier(@PathVariable Long supplierId) {
        return purchaseClient.getOrdersBySupplier(supplierId);
    }

    // --- Suppliers ---
    @GetMapping("/suppliers")
    public List<Map<String, Object>> viewSuppliers() {
        return supplierClient.getAllSuppliers();
    }

    @GetMapping("/suppliers/{id}")
    public Map<String, Object> viewSupplierDetail(@PathVariable Long id) {
        return supplierClient.getSupplierById(id);
    }

    @PostMapping("/suppliers")
    public Map<String, Object> addSupplier(@RequestBody Map<String, Object> supplier) {
        return supplierClient.addSupplier(supplier);
    }

    @PutMapping("/suppliers/{id}")
    public Map<String, Object> editSupplier(@PathVariable Long id, @RequestBody Map<String, Object> supplier) {
        return supplierClient.updateSupplier(id, supplier);
    }

    @DeleteMapping("/suppliers/{id}")
    public void deactivateSupplier(@PathVariable Long id) {
        supplierClient.deactivateSupplier(id);
    }
}
