package com.bridgelabz.movementservice.repository;

import com.bridgelabz.movementservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<Inventory> findAllByProductId(Long productId);
    List<Inventory> findAllByWarehouseId(Long warehouseId);
}
