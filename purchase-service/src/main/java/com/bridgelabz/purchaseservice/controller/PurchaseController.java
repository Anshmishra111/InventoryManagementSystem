package com.bridgelabz.purchaseservice.controller;

import com.bridgelabz.purchaseservice.entity.OrderStatus;
import com.bridgelabz.purchaseservice.entity.PurchaseOrder;
import com.bridgelabz.purchaseservice.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody PurchaseOrder order) {
        return ResponseEntity.ok(purchaseService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllOrders() {
        return ResponseEntity.ok(purchaseService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseOrder> updateStatus(
            @PathVariable Long id, 
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(purchaseService.updateStatus(id, status));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PurchaseOrder> approveOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.approveOrder(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrder> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.cancelOrder(id));
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receiveGoods(
            @PathVariable Long id,
            @RequestParam(required = false) Long warehouseId,
            @RequestBody(required = false) java.util.Map<Long, Integer> receivedItems) {
        return ResponseEntity.ok(purchaseService.receiveGoods(id, warehouseId, receivedItems));
    }
}
