package com.bridgelabz.salesservice.controller;

import com.bridgelabz.salesservice.entity.Customer;
import com.bridgelabz.salesservice.entity.SalesOrder;
import com.bridgelabz.salesservice.service.SalesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.salesservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesService salesService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        when(salesService.addCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/sales/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Customer"));
    }

    @Test
    void testPlaceOrder() throws Exception {
        SalesOrder order = new SalesOrder();
        order.setStatus("COMPLETED");

        when(salesService.placeOrder(any(SalesOrder.class))).thenReturn(order);

        mockMvc.perform(post("/api/sales/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(salesService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sales/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
