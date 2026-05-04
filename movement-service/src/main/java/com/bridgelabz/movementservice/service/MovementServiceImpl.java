package com.bridgelabz.movementservice.service;

import com.bridgelabz.movementservice.entity.Inventory;
import com.bridgelabz.movementservice.entity.MovementType;
import com.bridgelabz.movementservice.entity.StockMovement;
import com.bridgelabz.movementservice.repository.InventoryRepository;
import com.bridgelabz.movementservice.repository.StockMovementRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovementServiceImpl implements MovementService {

    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;

    public MovementServiceImpl(InventoryRepository inventoryRepository, StockMovementRepository stockMovementRepository) {
        this.inventoryRepository = inventoryRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "inventory", key = "#movement.productId"),
        @CacheEvict(value = "inventory", allEntries = true)
    })
    public StockMovement recordMovement(StockMovement movement) {
        movement.setTimestamp(LocalDateTime.now());
        
        Inventory inventory = inventoryRepository
                .findByProductIdAndWarehouseId(movement.getProductId(), movement.getWarehouseId())
                .orElse(new Inventory(null, movement.getProductId(), movement.getWarehouseId(), 0));

        if (movement.getType() == MovementType.IN) {
            inventory.setQuantity(inventory.getQuantity() + movement.getQuantity());
        } else if (movement.getType() == MovementType.OUT) {
            if (inventory.getQuantity() < movement.getQuantity()) {
                throw new RuntimeException("Insufficient stock in warehouse " + movement.getWarehouseId() 
                        + " for product " + movement.getProductId());
            }
            inventory.setQuantity(inventory.getQuantity() - movement.getQuantity());
        }
        // INTERNAL logic can be expanded here if needed (subtract from source, add to dest)

        inventoryRepository.save(inventory);
        return stockMovementRepository.save(movement);
    }

    @Override
    @Cacheable(value = "inventory", key = "#productId")
    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findAllByProductId(productId);
    }

    @Override
    public List<Inventory> getInventoryByWarehouseId(Long warehouseId) {
        return inventoryRepository.findAllByWarehouseId(warehouseId);
    }

    @Override
    public List<StockMovement> getMovementHistoryByProductId(Long productId) {
        return stockMovementRepository.findAllByProductId(productId);
    }
}
