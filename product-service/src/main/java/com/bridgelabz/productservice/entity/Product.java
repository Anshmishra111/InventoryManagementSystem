package com.bridgelabz.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;
    private String brand;
    private String unitOfMeasure; // e.g., kg, pieces, liters

    @Column(precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    private Integer currentStockLevel;
    private Integer reorderLevel;
    private Integer maxStockLevel;
    private Integer leadTimeDays;

    private String imageUrl;
    
    @Column(unique = true)
    private String barcode;

    @Builder.Default
    private boolean isActive = true;

    // Helper method for reorder alert
    public boolean needsReorder() {
        return currentStockLevel <= reorderLevel;
    }
}
