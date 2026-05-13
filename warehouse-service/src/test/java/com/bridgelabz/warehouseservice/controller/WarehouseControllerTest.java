package com.bridgelabz.warehouseservice.controller;

import com.bridgelabz.warehouseservice.entity.Warehouse;
import com.bridgelabz.warehouseservice.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.warehouseservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateWarehouse() throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Main Warehouse");

        when(warehouseService.createWarehouse(any(Warehouse.class))).thenReturn(warehouse);

        mockMvc.perform(post("/api/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void testGetWarehouseById() throws Exception {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Main Warehouse");

        when(warehouseService.getWarehouseById(1L)).thenReturn(warehouse);

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Warehouse"));
    }

    @Test
    void testGetAllActiveWarehouses() throws Exception {
        when(warehouseService.getAllActiveWarehouses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testDeleteWarehouse() throws Exception {
        mockMvc.perform(delete("/api/warehouses/1"))
                .andExpect(status().isOk());
    }
}
