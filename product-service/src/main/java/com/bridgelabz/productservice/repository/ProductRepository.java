package com.bridgelabz.productservice.repository;

import com.bridgelabz.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    Optional<Product> findByBarcode(String barcode);
    
    List<Product> findByIsActiveTrue();
    
    List<Product> findByCategoryIgnoreCase(String category);
    
    @Query("SELECT p FROM Product p WHERE p.currentStockLevel <= p.reorderLevel AND p.isActive = true")
    List<Product> findLowStockProducts();
    
    boolean existsBySku(String sku);
}
