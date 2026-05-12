package com.bridgelabz.movementservice.controller;

import com.bridgelabz.movementservice.entity.Inventory;
import com.bridgelabz.movementservice.entity.StockMovement;
import com.bridgelabz.movementservice.service.MovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<StockMovement> recordMovement(@RequestBody StockMovement movement) {
        return ResponseEntity.ok(movementService.recordMovement(movement));
    }

    @GetMapping("/inventory/product/{productId}")
    public ResponseEntity<List<Inventory>> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getInventoryByProductId(productId));
    }

    @GetMapping("/inventory/warehouse/{warehouseId}")
    public ResponseEntity<List<Inventory>> getInventoryByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(movementService.getInventoryByWarehouseId(warehouseId));
    }

    @GetMapping("/history/product/{productId}")
    public ResponseEntity<List<StockMovement>> getMovementHistoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(movementService.getMovementHistoryByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<List<StockMovement>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(movementService.getAllInventory());
    }
}
