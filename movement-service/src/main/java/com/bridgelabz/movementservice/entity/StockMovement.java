package com.bridgelabz.movementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long warehouseId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private LocalDateTime timestamp = LocalDateTime.now();
    
    private String reason; // Optional: e.g., "Purchase Order #123"
    
    private String performedBy;
    
    private Long referenceId;
    
    private String referenceType;
    
    private Long toWarehouseId;
    
    private String notes;
    
    private Double unitCost;
    
    private Integer balanceAfter;
}
