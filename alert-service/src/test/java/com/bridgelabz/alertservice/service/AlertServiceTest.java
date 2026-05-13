package com.bridgelabz.alertservice.service;

import com.bridgelabz.alertservice.client.WarehouseClient;
import com.bridgelabz.alertservice.entity.*;
import com.bridgelabz.alertservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @Mock
    private AlertConfigRepository alertConfigRepository;

    @Mock
    private AlertHistoryRepository alertHistoryRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private WarehouseClient warehouseClient;

    @InjectMocks
    private AlertServiceImpl alertService;

    private AlertConfig testConfig;

    @BeforeEach
    void setUp() {
        testConfig = new AlertConfig();
        testConfig.setProductId(1L);
        testConfig.setLowStockThreshold(10);
        testConfig.setAlertEmail("admin@example.com");
        testConfig.setEnabled(true);
    }

    @Test
    void testCreateOrUpdateConfig_New() {
        when(alertConfigRepository.findByProductId(1L)).thenReturn(Optional.empty());
        when(alertConfigRepository.save(any(AlertConfig.class))).thenReturn(testConfig);

        AlertConfig result = alertService.createOrUpdateConfig(testConfig);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        verify(alertConfigRepository, times(1)).save(any(AlertConfig.class));
    }

    @Test
    void testCreateOrUpdateConfig_Existing() {
        AlertConfig existingConfig = new AlertConfig();
        existingConfig.setProductId(1L);
        existingConfig.setLowStockThreshold(5);

        when(alertConfigRepository.findByProductId(1L)).thenReturn(Optional.of(existingConfig));
        when(alertConfigRepository.save(any(AlertConfig.class))).thenReturn(existingConfig);

        AlertConfig result = alertService.createOrUpdateConfig(testConfig);

        assertNotNull(result);
        assertEquals(10, existingConfig.getLowStockThreshold()); // Updated value
        verify(alertConfigRepository, times(1)).save(existingConfig);
    }

    @Test
    void testGetConfigByProductId_Found() {
        when(alertConfigRepository.findByProductId(1L)).thenReturn(Optional.of(testConfig));

        AlertConfig result = alertService.getConfigByProductId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
    }

    @Test
    void testGetConfigByProductId_NotFound() {
        when(alertConfigRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> alertService.getConfigByProductId(1L));
    }

    @Test
    void testLogAlert() {
        when(alertConfigRepository.findByProductId(1L)).thenReturn(Optional.of(testConfig));
        when(alertHistoryRepository.save(any(AlertHistory.class))).thenAnswer(i -> i.getArguments()[0]);

        AlertHistory history = alertService.logAlert(1L, 5, "Low stock!");

        assertNotNull(history);
        assertEquals(1L, history.getProductId());
        assertEquals(5, history.getCurrentQuantity());
        verify(alertHistoryRepository, times(1)).save(any(AlertHistory.class));
    }

    @Test
    void testMarkAsRead() {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setRead(false);

        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        alertService.markAsRead(1L);

        assertTrue(alert.isRead());
        verify(alertRepository, times(1)).save(alert);
    }
}
