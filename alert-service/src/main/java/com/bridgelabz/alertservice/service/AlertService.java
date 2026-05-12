package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.entity.AlertConfig;
import com.bridgelabz.alertservice.entity.AlertHistory;
import java.util.List;

public interface AlertService {
    AlertConfig createOrUpdateConfig(AlertConfig config);
    AlertConfig getConfigByProductId(Long productId);
    AlertHistory logAlert(Long productId, Integer currentQuantity, String message);
    void sendLowStockAlert(Long productId, String productName, Long warehouseId, String warehouseName, Integer currentQty, Integer reorderLevel);
    List<AlertHistory> getHistoryByProductId(Long productId);
    List<AlertConfig> getAllConfigs();
    
    // Management methods
    List<com.bridgelabz.alertservice.entity.Alert> getAllAlerts();
    List<com.bridgelabz.alertservice.entity.Alert> getAlertsByRecipient(Long recipientId);
    void markAsRead(Long alertId);
    void markAllAsRead(Long recipientId);
    void acknowledge(Long alertId);
    Long getUnreadCount(Long recipientId);
}
