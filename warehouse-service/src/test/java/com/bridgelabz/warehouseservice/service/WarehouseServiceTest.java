package com.bridgelabz.warehouseservice.service;

import com.bridgelabz.warehouseservice.entity.Warehouse;
import com.bridgelabz.warehouseservice.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse testWarehouse;

    @BeforeEach
    void setUp() {
        testWarehouse = new Warehouse();
        testWarehouse.setId(1L);
        testWarehouse.setName("Main Warehouse");
        testWarehouse.setLocation("New York");
        testWarehouse.setActive(true);
    }

    @Test
    void testCreateWarehouse() {
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);

        Warehouse result = warehouseService.createWarehouse(testWarehouse);

        assertNotNull(result);
        assertEquals("Main Warehouse", result.getName());
        verify(warehouseRepository, times(1)).save(testWarehouse);
    }

    @Test
    void testGetWarehouseById_Found() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));

        Warehouse result = warehouseService.getWarehouseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetWarehouseById_NotFound() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> warehouseService.getWarehouseById(1L));
    }

    @Test
    void testGetAllActiveWarehouses() {
        when(warehouseRepository.findByActiveTrue()).thenReturn(Collections.singletonList(testWarehouse));

        List<Warehouse> result = warehouseService.getAllActiveWarehouses();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteWarehouse() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);

        warehouseService.deleteWarehouse(1L);

        assertFalse(testWarehouse.isActive());
        verify(warehouseRepository, times(1)).save(testWarehouse);
    }
}
