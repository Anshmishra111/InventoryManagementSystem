package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.entity.*;
import com.bridgelabz.alertservice.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertConfigRepository alertConfigRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertConfigRepository alertConfigRepository, 
                            AlertHistoryRepository alertHistoryRepository,
                            AlertRepository alertRepository) {
        this.alertConfigRepository = alertConfigRepository;
        this.alertHistoryRepository = alertHistoryRepository;
        this.alertRepository = alertRepository;
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
    public void sendLowStockAlert(Long productId, Long warehouseId, Integer currentQty, Integer reorderLevel) {
        Alert alert = new Alert();
        alert.setRecipientId(1L); // Default admin
        alert.setRelatedProductId(productId);
        alert.setRelatedWarehouseId(warehouseId);
        alert.setTitle("Scheduled Low Stock Check");
        alert.setMessage("Low stock detected: Product " + productId + " has " + currentQty + " units (Threshold: " + reorderLevel + ")");
        alert.setSeverity(AlertSeverity.WARNING);
        alert.setType(AlertType.LOW_STOCK);
        alert.setCreatedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }
}
