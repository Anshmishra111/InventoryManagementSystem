package com.bridgelabz.alertservice.consumer;

import com.bridgelabz.alertservice.client.ProductClient;
import com.bridgelabz.alertservice.entity.Alert;
import com.bridgelabz.alertservice.repository.AlertRepository;
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

    private final AlertRepository alertRepository;
    private final ProductClient productClient;

    @RabbitListener(queues = "inventory.alert.queue")
    public void consumeStockMovement(Map<String, Object> event) {
        log.info("Received stock movement event: {}", event);
        try {
            Long productId = Long.valueOf(event.get("productId").toString());
            
            // Fetch current product details to check reorder level
            Map<String, Object> product = productClient.getProductById(productId);
            if (product == null) return;

            Integer currentStock = (Integer) product.get("currentStockLevel");
            Integer reorderLevel = (Integer) product.get("reorderLevel");

            if (currentStock != null && reorderLevel != null && currentStock <= reorderLevel) {
                log.warn("Low stock detected for product {}: Current={}, Reorder={}", 
                        product.get("name"), currentStock, reorderLevel);
                
                // Create alert if not already exists for this product (active)
                boolean alreadyAlerted = alertRepository.findAll().stream()
                        .anyMatch(a -> a.getRelatedProductId() != null && a.getRelatedProductId().equals(productId) && !a.isAcknowledged() && a.getType() == com.bridgelabz.alertservice.entity.AlertType.LOW_STOCK);

                if (!alreadyAlerted) {
                    Alert alert = new Alert();
                    alert.setRecipientId(1L);
                    alert.setRelatedProductId(productId);
                    alert.setTitle("Low Stock Alert");
                    alert.setMessage("Product " + product.get("name") + " is below reorder level. Current stock: " + currentStock);
                    alert.setSeverity(com.bridgelabz.alertservice.entity.AlertSeverity.CRITICAL);
                    alert.setType(com.bridgelabz.alertservice.entity.AlertType.LOW_STOCK);
                    alert.setCreatedAt(LocalDateTime.now());
                    alertRepository.save(alert);
                    log.info("Low stock alert created for product {}", productId);
                }
            }
        } catch (Exception e) {
            log.error("Error processing stock movement event: {}", e.getMessage());
        }
    }
}
