package com.bridgelabz.purchaseservice.service;

import com.bridgelabz.purchaseservice.entity.OrderStatus;
import com.bridgelabz.purchaseservice.entity.PurchaseOrder;
import java.util.List;

public interface PurchaseService {
    PurchaseOrder createOrder(PurchaseOrder order);
    List<PurchaseOrder> getAllOrders();
    PurchaseOrder getOrderById(Long id);
    PurchaseOrder updateStatus(Long id, OrderStatus status);
    PurchaseOrder approveOrder(Long id);
    PurchaseOrder cancelOrder(Long id);
    PurchaseOrder receiveGoods(Long id, Long warehouseId, java.util.Map<Long, Integer> receivedItems);
}
