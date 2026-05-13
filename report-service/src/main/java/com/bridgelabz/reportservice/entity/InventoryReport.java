package com.bridgelabz.reportservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportName;

    private LocalDateTime generatedAt = LocalDateTime.now();

    private Integer totalProducts;

    private Double totalStockValue;

    private String reportType; // e.g., "SUMMARY", "LOW_STOCK"

    @Column(columnDefinition = "TEXT")
    private String details; // Optional: JSON or text summary of findings
}
