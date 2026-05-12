package com.bridgelabz.productservice.messaging;

import com.bridgelabz.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockMovementConsumer {

    private final ProductService productService;

    @RabbitListener(queues = "inventory.stock.queue")
    public void consumeStockMovement(Map<String, Object> event) {
        log.info("Received stock movement for sync: {}", event);
        try {
            Long productId = Long.valueOf(event.get("productId").toString());
            Integer quantity = (Integer) event.get("quantity");
            String type = event.get("type").toString();
            Double unitCost = event.get("unitCost") != null ? Double.valueOf(event.get("unitCost").toString()) : null;

            // Calculate actual change based on type
            int change = quantity;
            if (type.equals("STOCK_OUT") || type.equals("TRANSFER_OUT") || type.equals("WRITE_OFF")) {
                change = -quantity;
            } else if (type.equals("INTERNAL")) {
                change = 0; // Internal transfers don't change TOTAL stock level
            }

            log.info("Updating product {} stock by {} with cost {}", productId, change, unitCost);
            productService.updateStock(productId, change, unitCost);
            
        } catch (Exception e) {
            log.error("Error syncing stock via RabbitMQ: {}", e.getMessage());
        }
    }
}
