package com.bridgelabz.movementservice.repository;

import com.bridgelabz.movementservice.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findAllByProductId(Long productId);
    List<StockMovement> findAllByWarehouseId(Long warehouseId);
}
