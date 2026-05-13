package com.bridgelabz.alertservice.consumer;

import com.bridgelabz.alertservice.client.ProductClient;
import com.bridgelabz.alertservice.service.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockMovementConsumerTest {

    @Mock
    private AlertService alertService;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private StockMovementConsumer stockMovementConsumer;

    @Test
    void testConsumeStockMovement_LowStock() {
        Map<String, Object> event = new HashMap<>();
        event.put("productId", 1L);
        event.put("newQuantity", 5);
        event.put("warehouseId", 10L);

        Map<String, Object> product = new HashMap<>();
        product.put("reorderLevel", 10);
        product.put("name", "Test Product");

        when(productClient.getProductById(1L)).thenReturn(product);

        stockMovementConsumer.consumeStockMovement(event);

        verify(alertService, times(1)).sendLowStockAlert(
                eq(1L), eq("Test Product"), eq(10L), isNull(), eq(5), eq(10));
    }

    @Test
    void testConsumeStockMovement_EnoughStock() {
        Map<String, Object> event = new HashMap<>();
        event.put("productId", 1L);
        event.put("newQuantity", 15);
        event.put("warehouseId", 10L);

        Map<String, Object> product = new HashMap<>();
        product.put("reorderLevel", 10);
        product.put("name", "Test Product");

        when(productClient.getProductById(1L)).thenReturn(product);

        stockMovementConsumer.consumeStockMovement(event);

        verify(alertService, never()).sendLowStockAlert(anyLong(), anyString(), anyLong(), anyString(), anyInt(), anyInt());
    }
}
