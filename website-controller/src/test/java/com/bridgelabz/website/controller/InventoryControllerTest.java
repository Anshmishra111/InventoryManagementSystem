package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.*;
import com.bridgelabz.website.config.GatewayJwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductClient productClient;

    @MockBean
    private WarehouseClient warehouseClient;

    @MockBean
    private MovementClient movementClient;

    @MockBean
    private AlertClient alertClient;

    @MockBean
    private ReportClient reportClient;

    @MockBean
    private GatewayJwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProducts() throws Exception {
        Map<String, Object> product = new HashMap<>();
        product.put("id", 1);
        product.put("name", "Test Product");

        when(productClient.getAllProducts()).thenReturn(Collections.singletonList(product));
        when(movementClient.getInventoryByProductId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dashboard/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Map<String, Object> product = new HashMap<>();
        product.put("name", "New Product");

        when(productClient.createProduct(any())).thenReturn(product);

        mockMvc.perform(post("/api/dashboard/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void testGetAlerts() throws Exception {
        when(alertClient.getAllAlerts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/dashboard/inventory/alerts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
