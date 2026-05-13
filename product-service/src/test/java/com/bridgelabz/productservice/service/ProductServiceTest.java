package com.bridgelabz.productservice.service;

import com.bridgelabz.productservice.entity.Product;
import com.bridgelabz.productservice.repository.ProductRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .sku("SKU123")
                .currentStockLevel(50)
                .reorderLevel(10)
                .isActive(true)
                .build();
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals("SKU123", result.getSku());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_AlreadyExists() {
        when(productRepository.existsBySku(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> productService.createProduct(testProduct));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testUpdateStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateStock(1L, 10, 100.0);

        assertEquals(60, testProduct.getCurrentStockLevel());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testSoftDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.softDeleteProduct(1L);

        assertFalse(testProduct.isActive());
        verify(productRepository, times(1)).save(testProduct);
    }
}
