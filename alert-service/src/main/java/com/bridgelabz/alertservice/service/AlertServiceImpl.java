package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.entity.AlertConfig;
import com.bridgelabz.alertservice.entity.AlertHistory;
import com.bridgelabz.alertservice.repository.AlertConfigRepository;
import com.bridgelabz.alertservice.repository.AlertHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertConfigRepository alertConfigRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final com.bridgelabz.alertservice.repository.AlertRepository alertRepository;

    public AlertServiceImpl(AlertConfigRepository alertConfigRepository, 
                            AlertHistoryRepository alertHistoryRepository,
                            com.bridgelabz.alertservice.repository.AlertRepository alertRepository) {
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
        com.bridgelabz.alertservice.entity.Alert alert = new com.bridgelabz.alertservice.entity.Alert();
        alert.setRecipientId(1L); // Default admin
        alert.setRelatedProductId(productId);
        alert.setRelatedWarehouseId(warehouseId);
        alert.setTitle("Scheduled Low Stock Check");
        alert.setMessage("Low stock detected: Product " + productId + " has " + currentQty + " units (Threshold: " + reorderLevel + ")");
        alert.setSeverity(com.bridgelabz.alertservice.entity.AlertSeverity.WARNING);
        alert.setType(com.bridgelabz.alertservice.entity.AlertType.LOW_STOCK);
        alert.setCreatedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }
}
