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

    public PurchaseServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public PurchaseOrder createOrder(PurchaseOrder order) {
        order.setOrderNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderDate(LocalDateTime.now());
        
        // Ensure child items have back-reference and calculate total
        double total = 0;
        for (PurchaseOrderItem item : order.getItems()) {
            item.setPurchaseOrder(order);
            total += (item.getQuantity() * item.getUnitPrice());
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
        order.setStatus(status);
        return purchaseOrderRepository.save(order);
    }
}
