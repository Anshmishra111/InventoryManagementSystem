package com.bridgelabz.movementservice.controller;

import com.bridgelabz.movementservice.entity.MovementType;
import com.bridgelabz.movementservice.entity.StockMovement;
import com.bridgelabz.movementservice.service.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.movementservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovementService movementService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRecordMovement() throws Exception {
        StockMovement movement = new StockMovement();
        movement.setProductId(1L);
        movement.setWarehouseId(1L);
        movement.setQuantity(10);
        movement.setType(MovementType.IN);

        when(movementService.recordMovement(any(StockMovement.class))).thenReturn(movement);

        mockMvc.perform(post("/api/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void testGetInventoryByProductId() throws Exception {
        when(movementService.getInventoryByProductId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/movements/inventory/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAllInventory() throws Exception {
        when(movementService.getAllInventory()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/movements/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
