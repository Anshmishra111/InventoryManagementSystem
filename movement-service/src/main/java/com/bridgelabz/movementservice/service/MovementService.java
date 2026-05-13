package com.bridgelabz.movementservice.service;

import com.bridgelabz.movementservice.entity.Inventory;
import com.bridgelabz.movementservice.entity.StockMovement;
import java.util.List;

public interface MovementService {
    StockMovement recordMovement(StockMovement movement);
    List<Inventory> getInventoryByProductId(Long productId);
    List<Inventory> getInventoryByWarehouseId(Long warehouseId);
    List<StockMovement> getMovementHistoryByProductId(Long productId);
    List<Inventory> getAllInventory();
    List<StockMovement> getAllMovements();
}
