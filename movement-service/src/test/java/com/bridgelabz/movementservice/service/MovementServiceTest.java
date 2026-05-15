package com.bridgelabz.movementservice.service;

import com.bridgelabz.movementservice.entity.Inventory;
import com.bridgelabz.movementservice.entity.MovementType;
import com.bridgelabz.movementservice.entity.StockMovement;
import com.bridgelabz.movementservice.repository.InventoryRepository;
import com.bridgelabz.movementservice.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private MovementServiceImpl movementService;

    private StockMovement testMovement;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        testMovement = new StockMovement();
        testMovement.setProductId(1L);
        testMovement.setWarehouseId(1L);
        testMovement.setQuantity(10);
        testMovement.setType(MovementType.IN);

        testInventory = new Inventory(1L, 1L, 1L, 50);
    }

    @Test
    void testRecordMovement_StockIn() {
        when(inventoryRepository.findByProductIdAndWarehouseId(1L, 1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(testMovement);

        StockMovement result = movementService.recordMovement(testMovement);

        assertNotNull(result);
        assertEquals(60, testInventory.getQuantity()); // 50 + 10
        verify(inventoryRepository, times(1)).save(testInventory);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyMap());
    }

    @Test
    void testRecordMovement_StockOut_Success() {
        testMovement.setType(MovementType.OUT);
        when(inventoryRepository.findByProductIdAndWarehouseId(1L, 1L)).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(testMovement);

        StockMovement result = movementService.recordMovement(testMovement);

        assertNotNull(result);
        assertEquals(40, testInventory.getQuantity()); // 50 - 10
    }

    @Test
    void testRecordMovement_InsufficientStock() {
        testMovement.setType(MovementType.OUT);
        testMovement.setQuantity(100);
        when(inventoryRepository.findByProductIdAndWarehouseId(1L, 1L)).thenReturn(Optional.of(testInventory));

        assertThrows(RuntimeException.class, () -> movementService.recordMovement(testMovement));
        verify(stockMovementRepository, never()).save(any());
    }

    @Test
    void testGetInventoryByProductId() {
        when(inventoryRepository.findAllByProductId(1L)).thenReturn(Collections.singletonList(testInventory));

        List<Inventory> result = movementService.getInventoryByProductId(1L);

        assertEquals(1, result.size());
    }
}
