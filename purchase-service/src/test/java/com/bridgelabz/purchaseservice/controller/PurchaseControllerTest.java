package com.bridgelabz.purchaseservice.controller;

import com.bridgelabz.purchaseservice.entity.OrderStatus;
import com.bridgelabz.purchaseservice.entity.PurchaseOrder;
import com.bridgelabz.purchaseservice.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.purchaseservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNumber("PO-123");

        when(purchaseService.createOrder(any(PurchaseOrder.class))).thenReturn(order);

        mockMvc.perform(post("/api/purchase-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("PO-123"));
    }

    @Test
    void testGetOrderById() throws Exception {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(1L);

        when(purchaseService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/purchase-orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateStatus() throws Exception {
        PurchaseOrder order = new PurchaseOrder();
        order.setStatus(OrderStatus.FULLY_RECEIVED);

        when(purchaseService.updateStatus(anyLong(), any(OrderStatus.class))).thenReturn(order);

        mockMvc.perform(put("/api/purchase-orders/1/status")
                .param("status", "FULLY_RECEIVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FULLY_RECEIVED"));
    }

    @Test
    void testApproveOrder() throws Exception {
        PurchaseOrder order = new PurchaseOrder();
        order.setStatus(OrderStatus.APPROVED);

        when(purchaseService.approveOrder(1L)).thenReturn(order);

        mockMvc.perform(put("/api/purchase-orders/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
