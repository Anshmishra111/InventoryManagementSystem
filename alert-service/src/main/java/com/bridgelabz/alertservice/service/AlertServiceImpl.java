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

    public AlertServiceImpl(AlertConfigRepository alertConfigRepository, AlertHistoryRepository alertHistoryRepository) {
        this.alertConfigRepository = alertConfigRepository;
        this.alertHistoryRepository = alertHistoryRepository;
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
}
