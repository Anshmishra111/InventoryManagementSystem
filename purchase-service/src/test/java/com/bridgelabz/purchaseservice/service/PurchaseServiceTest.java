package com.bridgelabz.purchaseservice.service;

import com.bridgelabz.purchaseservice.client.MovementClient;
import com.bridgelabz.purchaseservice.entity.OrderStatus;
import com.bridgelabz.purchaseservice.entity.PurchaseOrder;
import com.bridgelabz.purchaseservice.entity.PurchaseOrderItem;
import com.bridgelabz.purchaseservice.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private MovementClient movementClient;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private PurchaseOrder testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new PurchaseOrder();
        testOrder.setId(1L);
        testOrder.setOrderNumber("PO-TEST123");
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setWarehouseId(1L);

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setProductId(1L);
        item.setQuantity(10);
        item.setUnitPrice(100.0);
        
        List<PurchaseOrderItem> items = new ArrayList<>();
        items.add(item);
        testOrder.setItems(items);
    }

    @Test
    void testCreateOrder() {
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenAnswer(i -> i.getArguments()[0]);

        PurchaseOrder result = purchaseService.createOrder(testOrder);

        assertNotNull(result.getOrderNumber());
        assertTrue(result.getOrderNumber().startsWith("PO-"));
        assertEquals(1000.0, result.getTotalAmount());
        verify(purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testUpdateStatus_ToReceived() {
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(testOrder);

        PurchaseOrder result = purchaseService.updateStatus(1L, OrderStatus.FULLY_RECEIVED);

        assertEquals(OrderStatus.FULLY_RECEIVED, result.getStatus());
        verify(movementClient, times(1)).recordMovement(anyMap());
        verify(purchaseOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testGetOrderById_Found() {
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        PurchaseOrder result = purchaseService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testApproveOrder() {
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(testOrder);

        PurchaseOrder result = purchaseService.approveOrder(1L);

        assertEquals(OrderStatus.APPROVED, result.getStatus());
    }
}
