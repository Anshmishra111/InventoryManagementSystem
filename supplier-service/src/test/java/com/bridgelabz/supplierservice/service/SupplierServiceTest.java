package com.bridgelabz.supplierservice.service;

import com.bridgelabz.supplierservice.entity.Supplier;
import com.bridgelabz.supplierservice.repository.SupplierRepository;
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
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        testSupplier = new Supplier();
        testSupplier.setId(1L);
        testSupplier.setName("Test Supplier");
        testSupplier.setEmail("supplier@example.com");
        testSupplier.setActive(true);
    }

    @Test
    void testAddSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

        Supplier result = supplierService.addSupplier(testSupplier);

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
        verify(supplierRepository, times(1)).save(testSupplier);
    }

    @Test
    void testGetSupplierById_Found() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));

        Supplier result = supplierService.getSupplierById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

        Supplier details = new Supplier();
        details.setName("Updated Supplier");

        Supplier result = supplierService.updateSupplier(1L, details);

        assertEquals("Updated Supplier", result.getName());
        verify(supplierRepository, times(1)).save(testSupplier);
    }

    @Test
    void testDeactivateSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

        supplierService.deactivateSupplier(1L);

        assertFalse(testSupplier.isActive());
        verify(supplierRepository, times(1)).save(testSupplier);
    }
}
