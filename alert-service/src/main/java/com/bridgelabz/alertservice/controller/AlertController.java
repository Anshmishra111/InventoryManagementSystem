package com.bridgelabz.alertservice.controller;

import com.bridgelabz.alertservice.entity.AlertConfig;
import com.bridgelabz.alertservice.entity.AlertHistory;
import com.bridgelabz.alertservice.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/configs")
    public ResponseEntity<AlertConfig> createOrUpdateConfig(@RequestBody AlertConfig config) {
        return ResponseEntity.ok(alertService.createOrUpdateConfig(config));
    }

    @GetMapping("/configs")
    public ResponseEntity<List<AlertConfig>> getAllConfigs() {
        return ResponseEntity.ok(alertService.getAllConfigs());
    }

    @GetMapping("/configs/{productId}")
    public ResponseEntity<AlertConfig> getConfigByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(alertService.getConfigByProductId(productId));
    }

    @PostMapping("/trigger/{productId}")
    public ResponseEntity<AlertHistory> triggerAlert(
            @PathVariable Long productId,
            @RequestParam Integer currentQuantity,
            @RequestParam String message) {
        return ResponseEntity.ok(alertService.logAlert(productId, currentQuantity, message));
    }

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<AlertHistory>> getHistoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(alertService.getHistoryByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<List<com.bridgelabz.alertservice.entity.Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<com.bridgelabz.alertservice.entity.Alert>> getAlertsByRecipient(@PathVariable Long recipientId) {
        return ResponseEntity.ok(alertService.getAlertsByRecipient(recipientId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        alertService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllRead() {
        // Defaulting to user 1 for now
        alertService.markAllAsRead(1L);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<Void> acknowledge(@PathVariable Long id) {
        alertService.acknowledge(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recipient/{recipientId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long recipientId) {
        return ResponseEntity.ok(alertService.getUnreadCount(recipientId));
    }
}
