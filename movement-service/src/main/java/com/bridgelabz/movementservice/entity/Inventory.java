package com.bridgelabz.movementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long warehouseId;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    private String location; // bin/aisle ref

    @Version
    private Integer version;

    public Inventory(Long id, Long productId, Long warehouseId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.reservedQuantity = 0;
    }

    public Integer getAvailableQuantity() {
        return (quantity != null ? quantity : 0) - (reservedQuantity != null ? reservedQuantity : 0);
    }
}
