package com.bridgelabz.alertservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private String title;

    private String message;

    private Long relatedProductId;

    private Long relatedWarehouseId;

    private String channel; // e.g. "WEB", "EMAIL"

    private boolean isRead = false;

    private boolean isAcknowledged = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
