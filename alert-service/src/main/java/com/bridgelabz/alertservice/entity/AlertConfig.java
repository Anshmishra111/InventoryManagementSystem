package com.bridgelabz.alertservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alert_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    private String alertEmail;

    private boolean enabled = true;
}
