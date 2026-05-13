package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.client.WarehouseClient;
import com.bridgelabz.alertservice.entity.*;
import com.bridgelabz.alertservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AlertServiceImpl implements AlertService {

    private final AlertConfigRepository alertConfigRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertRepository alertRepository;
    private final WarehouseClient warehouseClient;

    public AlertServiceImpl(AlertConfigRepository alertConfigRepository, 
                            AlertHistoryRepository alertHistoryRepository,
                            AlertRepository alertRepository,
                            WarehouseClient warehouseClient) {
        this.alertConfigRepository = alertConfigRepository;
        this.alertHistoryRepository = alertHistoryRepository;
        this.alertRepository = alertRepository;
        this.warehouseClient = warehouseClient;
    }

    @Override
    public AlertConfig createOrUpdateConfig(AlertConfig newConfig) {
        return alertConfigRepository.findByProductId(newConfig.getProductId())
                .map(existing -> {
                    existing.setLowStockThreshold(newConfig.getLowStockThreshold());
                    existing.setAlertEmail(newConfig.getAlertEmail());
                    existing.setEnabled(newConfig.isEnabled());
                    return alertConfigRepository.save(existing);
                })
                .orElseGet(() -> alertConfigRepository.save(newConfig));
    }

    @Override
    public AlertConfig getConfigByProductId(Long productId) {
        return alertConfigRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Alert configuration not found for product: " + productId));
    }

    @Override
    public AlertHistory logAlert(Long productId, Integer currentQuantity, String message) {
        AlertConfig config = getConfigByProductId(productId);
        AlertHistory history = new AlertHistory();
        history.setProductId(productId);
        history.setCurrentQuantity(currentQuantity);
        history.setThresholdAtTime(config.getLowStockThreshold());
        history.setTriggeredAt(LocalDateTime.now());
        history.setMessage(message);
        return alertHistoryRepository.save(history);
    }

    @Override
    public List<AlertHistory> getHistoryByProductId(Long productId) {
        return alertHistoryRepository.findAllByProductId(productId);
    }

    @Override
    public List<AlertConfig> getAllConfigs() {
        return alertConfigRepository.findAll();
    }

    @Override
    public void sendLowStockAlert(Long productId, String productName, Long warehouseId, String warehouseName, Integer currentQty, Integer reorderLevel) {
        // Fetch warehouse name if not provided or placeholder
        if (warehouseId != null && (warehouseName == null || warehouseName.startsWith("Warehouse #"))) {
            try {
                java.util.Map<String, Object> warehouse = warehouseClient.getWarehouseById(warehouseId);
                if (warehouse != null && warehouse.get("name") != null) {
                    warehouseName = warehouse.get("name").toString();
                }
            } catch (Exception e) {
                log.warn("Failed to fetch warehouse name for ID {}: {}", warehouseId, e.getMessage());
            }
        }
        
        if (warehouseName == null) warehouseName = "Warehouse #" + warehouseId;

        // Deduplication & Refresh Logic:
        // 1. If there's an UNREAD alert for this product + warehouse, update its message instead of skipping.
        
        List<Alert> existingAlerts = alertRepository.findByRelatedProductIdAndRelatedWarehouseId(productId, warehouseId);
        
        Alert unreadAlert = existingAlerts.stream()
                .filter(a -> !a.isRead())
                .findFirst()
                .orElse(null);

        if (unreadAlert != null) {
            unreadAlert.setMessage("Low stock detected: " + productName + " has only " + currentQty + " units left at " + warehouseName + "!");
            unreadAlert.setCreatedAt(LocalDateTime.now()); // Bump the timestamp
            alertRepository.save(unreadAlert);
            log.info("Updated existing unread alert for {} in {}: {}", productName, warehouseName, unreadAlert.getMessage());
            return;
        }

        // 2. If no unread alert exists, create a new one.
        // We've removed the time-based lockout to ensure real-time responsiveness 
        // when the user is actively managing stock.

        Alert alert = new Alert();
        alert.setRecipientId(1L); // Default admin
        alert.setRelatedProductId(productId);
        alert.setRelatedWarehouseId(warehouseId);
        alert.setTitle("Low Stock Alert");
        alert.setMessage("Low stock detected: " + productName + " has only " + currentQty + " units left at " + warehouseName + "!");
        alert.setSeverity(AlertSeverity.WARNING);
        alert.setType(AlertType.LOW_STOCK);
        alert.setCreatedAt(LocalDateTime.now());
        alert.setRead(false);
        alertRepository.save(alert);
        log.info("New low stock alert created: {}", alert.getMessage());
    }

    @Override
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @Override
    public List<Alert> getAlertsByRecipient(Long recipientId) {
        return alertRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
    }

    @Override
    public void markAsRead(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            alert.setRead(true);
            alertRepository.save(alert);
        });
    }

    @Override
    public void markAllAsRead(Long recipientId) {
        List<Alert> unread = alertRepository.findByRecipientIdAndIsReadFalse(recipientId);
        unread.forEach(a -> a.setRead(true));
        alertRepository.saveAll(unread);
    }

    @Override
    public void acknowledge(Long alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            alert.setAcknowledged(true);
            alertRepository.save(alert);
        });
    }

    @Override
    public Long getUnreadCount(Long recipientId) {
        return alertRepository.countByRecipientIdAndIsReadFalse(recipientId);
    }
}
