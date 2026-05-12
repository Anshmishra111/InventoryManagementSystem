package com.bridgelabz.alertservice.scheduler;

import com.bridgelabz.alertservice.client.MovementClient;
import com.bridgelabz.alertservice.client.ProductClient;
import com.bridgelabz.alertservice.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockCheckScheduler {

    private final AlertService alertService;
    private final ProductClient productClient;
    private final MovementClient movementClient;

    /**
     * Periodically check stock levels across all warehouses.
     * Runs every 5 minutes.
     */
    @Scheduled(fixedRate = 60000)
    public void checkAllStockLevels() {
        log.info("Starting scheduled stock level check...");
        try {
            // 1. Fetch all products to get reorder levels
            List<Map<String, Object>> products = productClient.getAllProducts();
            Map<Long, Map<String, Object>> productMap = products.stream()
                    .collect(Collectors.toMap(
                            p -> Long.valueOf(p.get("id").toString()),
                            p -> p
                    ));

            // 2. Fetch all inventory records from movement-service
            List<Map<String, Object>> inventoryList = movementClient.getAllInventory();

            for (Map<String, Object> inv : inventoryList) {
                Long productId = Long.valueOf(inv.get("productId").toString());
                Long warehouseId = Long.valueOf(inv.get("warehouseId").toString());
                Integer currentQty = ((Number) inv.get("quantity")).intValue();

                Map<String, Object> product = productMap.get(productId);
                if (product != null) {
                    Object reorderObj = product.get("reorderLevel");
                    if (reorderObj != null) {
                        int reorderLevel = ((Number) reorderObj).intValue();
                        String productName = (String) product.get("name");

                        if (currentQty <= reorderLevel) {
                            log.warn("Scheduled low stock detected: {} in warehouse {} (Qty: {}, Threshold: {})", 
                                    productName, warehouseId, currentQty, reorderLevel);
                            
                            // This method handles deduplication (won't create duplicate alerts)
                            alertService.sendLowStockAlert(productId, productName, warehouseId, null, currentQty, reorderLevel);
                        }
                    }
                }
            }
            log.info("Scheduled stock level check completed.");
        } catch (Exception e) {
            log.error("Error during scheduled stock level check: {}", e.getMessage());
        }
    }
}
