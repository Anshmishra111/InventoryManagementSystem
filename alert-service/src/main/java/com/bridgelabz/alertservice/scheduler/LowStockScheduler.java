package com.bridgelabz.alertservice.scheduler;

import com.bridgelabz.alertservice.client.MovementClient;
import com.bridgelabz.alertservice.client.ProductClient;
import com.bridgelabz.alertservice.client.WarehouseClient;
import com.bridgelabz.alertservice.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LowStockScheduler {

    private final MovementClient movementClient;
    private final ProductClient productClient;
    private final WarehouseClient warehouseClient;
    private final AlertService alertService;

    // Run every 15 minutes
    @Scheduled(fixedRate = 900000)
    public void checkLowStock() {
        log.info("Starting scheduled low-stock check...");
        
        try {
            List<Map<String, Object>> inventory = movementClient.getAllInventory();
            
            for (Map<String, Object> stock : inventory) {
                Long productId = ((Number) stock.get("productId")).longValue();
                Integer currentQty = (Integer) stock.get("quantity");
                Long warehouseId = ((Number) stock.get("warehouseId")).longValue();
                
                Map<String, Object> product = productClient.getProductById(productId);
                Integer reorderLevel = (Integer) product.get("reorderLevel");
                String productName = (String) product.get("name");
                
                Map<String, Object> warehouse = warehouseClient.getWarehouseById(warehouseId);
                String warehouseName = (String) warehouse.get("name");
                
                if (reorderLevel != null && currentQty < reorderLevel) {
                    log.warn("Low stock detected for {} in {}. Current: {}, Threshold: {}", 
                            productName, warehouseName, currentQty, reorderLevel);
                    alertService.sendLowStockAlert(productId, productName, warehouseId, warehouseName, currentQty, reorderLevel);
                }
            }
        } catch (Exception e) {
            log.error("Error during low-stock check: {}", e.getMessage());
        }
    }
}
