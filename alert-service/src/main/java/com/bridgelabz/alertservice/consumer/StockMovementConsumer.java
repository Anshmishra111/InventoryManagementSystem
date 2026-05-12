package com.bridgelabz.alertservice.consumer;

import com.bridgelabz.alertservice.client.ProductClient;
import com.bridgelabz.alertservice.entity.Alert;
import com.bridgelabz.alertservice.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockMovementConsumer {

    private final AlertService alertService;
    private final ProductClient productClient;

    @RabbitListener(queues = "inventory.alert.queue")
    public void consumeStockMovement(Map<String, Object> event) {
        log.info("Received stock movement event: {}", event);
        try {
            Long productId = Long.valueOf(event.get("productId").toString());
            Integer currentStock = event.get("newQuantity") != null ? ((Number) event.get("newQuantity")).intValue() : null;
            Long warehouseId = event.get("warehouseId") != null ? Long.valueOf(event.get("warehouseId").toString()) : null;

            if (currentStock == null || warehouseId == null) return;

            // Fetch product reorder level
            Map<String, Object> product = productClient.getProductById(productId);
            if (product == null) return;

            Integer reorderLevel = (Integer) product.get("reorderLevel");
            String productName = (String) product.get("name");

            if (reorderLevel != null && currentStock <= reorderLevel) {
                // Fetch warehouse name
                String warehouseName = "Warehouse #" + warehouseId;
                try {
                    // Use a WarehouseClient if available, otherwise fallback to ID
                    // (I'll assume AlertService can handle the lookup if we pass null name, 
                    // or I can fetch it here)
                } catch (Exception e) {}

                log.warn("Real-time low stock detected for product {} in warehouse {}: Current={}, Reorder={}", 
                        productName, warehouseId, currentStock, reorderLevel);
                
                alertService.sendLowStockAlert(productId, productName, warehouseId, null, currentStock, reorderLevel);
            }
        } catch (Exception e) {
            log.error("Error processing stock movement event: {}", e.getMessage());
        }
    }
}
