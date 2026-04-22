package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.entity.AlertConfig;
import com.bridgelabz.alertservice.entity.AlertHistory;
import java.util.List;

public interface AlertService {
    AlertConfig createOrUpdateConfig(AlertConfig config);
    AlertConfig getConfigByProductId(Long productId);
    AlertHistory logAlert(Long productId, Integer currentQuantity, String message);
    List<AlertHistory> getHistoryByProductId(Long productId);
    List<AlertConfig> getAllConfigs();
}
