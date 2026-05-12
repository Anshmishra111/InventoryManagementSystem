package com.bridgelabz.purchaseservice.service;

import com.bridgelabz.purchaseservice.entity.OrderStatus;
import com.bridgelabz.purchaseservice.entity.PurchaseOrder;
import com.bridgelabz.purchaseservice.entity.PurchaseOrderItem;
import com.bridgelabz.purchaseservice.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final com.bridgelabz.purchaseservice.client.MovementClient movementClient;

    public PurchaseServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                               com.bridgelabz.purchaseservice.client.MovementClient movementClient) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.movementClient = movementClient;
    }

    @Override
    public PurchaseOrder createOrder(PurchaseOrder order) {
        order.setOrderNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderDate(LocalDateTime.now());
        
        // Ensure child items have back-reference and calculate total
        double total = 0;
        for (PurchaseOrderItem item : order.getItems()) {
            item.setPurchaseOrder(order);
            Double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
            total += (item.getQuantity() * price);
        }
        order.setTotalAmount(total);
        
        return purchaseOrderRepository.save(order);
    }

    @Override
    public List<PurchaseOrder> getAllOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    public PurchaseOrder getOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
    }

    @Override
    public PurchaseOrder updateStatus(Long id, OrderStatus status) {
        PurchaseOrder order = getOrderById(id);
        
        // Map RECEIVED from frontend to FULLY_RECEIVED if necessary
        if (status.name().equals("RECEIVED")) {
            status = OrderStatus.FULLY_RECEIVED;
        }

        order.setStatus(status);
        PurchaseOrder saved = purchaseOrderRepository.save(order);

        // If received, update stock via Movement Service
        if (status == OrderStatus.FULLY_RECEIVED || status == OrderStatus.PARTIALLY_RECEIVED) {
            for (PurchaseOrderItem item : order.getItems()) {
                try {
                    java.util.Map<String, Object> movement = new java.util.HashMap<>();
                    movement.put("productId", item.getProductId());
                    movement.put("warehouseId", order.getWarehouseId() != null ? order.getWarehouseId() : 1L); // Default to 1 if not set
                    movement.put("quantity", item.getQuantity());
                    movement.put("type", "IN");
                    movement.put("reason", "Purchase Order #" + order.getOrderNumber());
                    movementClient.recordMovement(movement);
                } catch (Exception e) {
                    // Log error but don't fail the status update
                    System.err.println("Failed to record movement for PO #" + order.getOrderNumber() + ": " + e.getMessage());
                }
            }
        }
        
        return saved;
    }
}
