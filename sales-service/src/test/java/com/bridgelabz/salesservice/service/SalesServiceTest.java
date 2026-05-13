package com.bridgelabz.salesservice.service;

import com.bridgelabz.salesservice.entity.Customer;
import com.bridgelabz.salesservice.entity.SalesOrder;
import com.bridgelabz.salesservice.repository.CustomerRepository;
import com.bridgelabz.salesservice.repository.SalesOrderRepository;
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
public class SalesServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @InjectMocks
    private SalesServiceImpl salesService;

    private Customer testCustomer;
    private SalesOrder testOrder;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("customer@example.com");

        testOrder = new SalesOrder();
        testOrder.setId(1L);
        testOrder.setCustomerId(1L);
        testOrder.setStatus("PENDING");
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer result = salesService.addCustomer(testCustomer);

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    void testPlaceOrder() {
        when(salesOrderRepository.save(any(SalesOrder.class))).thenReturn(testOrder);

        SalesOrder result = salesService.placeOrder(testOrder);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        verify(salesOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateStatus() {
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenReturn(testOrder);

        SalesOrder result = salesService.updateStatus(1L, "SHIPPED");

        assertEquals("SHIPPED", result.getStatus());
        verify(salesOrderRepository, times(1)).save(testOrder);
    }
}
