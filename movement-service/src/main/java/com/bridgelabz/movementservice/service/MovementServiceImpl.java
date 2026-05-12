package com.bridgelabz.movementservice.service;

import com.bridgelabz.movementservice.entity.Inventory;
import com.bridgelabz.movementservice.entity.MovementType;
import com.bridgelabz.movementservice.entity.StockMovement;
import com.bridgelabz.movementservice.repository.InventoryRepository;
import com.bridgelabz.movementservice.repository.StockMovementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MovementServiceImpl implements MovementService {
    public static final String EXCHANGE = "inventory.exchange";
    public static final String ROUTING_KEY = "inventory.movement.key";

    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final RabbitTemplate rabbitTemplate;

    public MovementServiceImpl(InventoryRepository inventoryRepository, StockMovementRepository stockMovementRepository, RabbitTemplate rabbitTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.rabbitTemplate = rabbitTemplate;
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

        if (movement.getType() == MovementType.IN || movement.getType() == MovementType.STOCK_IN || movement.getType() == MovementType.TRANSFER_IN) {
            inventory.setQuantity(inventory.getQuantity() + movement.getQuantity());
        } else if (movement.getType() == MovementType.OUT || movement.getType() == MovementType.STOCK_OUT || movement.getType() == MovementType.TRANSFER_OUT) {
            if (inventory.getQuantity() < movement.getQuantity()) {
                throw new RuntimeException("Insufficient stock in warehouse " + movement.getWarehouseId() 
                        + " for product " + movement.getProductId());
            }
            inventory.setQuantity(inventory.getQuantity() - movement.getQuantity());
        } else if (movement.getType() == MovementType.INTERNAL || movement.getType() == MovementType.TRANSFER_IN || movement.getType() == MovementType.TRANSFER_OUT) {
            // Handle Internal Transfer
            if (movement.getToWarehouseId() == null) {
                throw new RuntimeException("Destination warehouse (toWarehouseId) is required for internal transfer");
            }
            
            // 1. Subtract from source
            if (inventory.getQuantity() < movement.getQuantity()) {
                throw new RuntimeException("Insufficient stock in source warehouse " + movement.getWarehouseId());
            }
            inventory.setQuantity(inventory.getQuantity() - movement.getQuantity());
            
            // 2. Add to destination
            Inventory destInventory = inventoryRepository
                    .findByProductIdAndWarehouseId(movement.getProductId(), movement.getToWarehouseId())
                    .orElse(new Inventory(null, movement.getProductId(), movement.getToWarehouseId(), 0));
            destInventory.setQuantity(destInventory.getQuantity() + movement.getQuantity());
            inventoryRepository.save(destInventory);
        }

        inventoryRepository.save(inventory);
        StockMovement savedMovement = stockMovementRepository.save(movement);
        
        // Emit event to RabbitMQ for Alert Service and others
        try {
            java.util.Map<String, Object> event = new java.util.HashMap<>();
            event.put("productId", savedMovement.getProductId());
            event.put("quantity", savedMovement.getQuantity());
            event.put("warehouseId", savedMovement.getWarehouseId());
            event.put("toWarehouseId", savedMovement.getToWarehouseId());
            event.put("newQuantity", inventory.getQuantity());
            event.put("type", savedMovement.getType().toString());
            event.put("timestamp", savedMovement.getTimestamp());
            
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
            log.info("Emitted stock movement event: {}", event);
        } catch (Exception e) {
            log.error("Failed to emit stock movement event", e);
        }

        return savedMovement;
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

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAll();
    }
}
