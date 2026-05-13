package com.bridgelabz.alertservice.controller;

import com.bridgelabz.alertservice.entity.AlertConfig;
import com.bridgelabz.alertservice.service.AlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.alertservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simplicity
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrUpdateConfig() throws Exception {
        AlertConfig config = new AlertConfig();
        config.setProductId(1L);
        config.setLowStockThreshold(10);

        when(alertService.createOrUpdateConfig(any(AlertConfig.class))).thenReturn(config);

        mockMvc.perform(post("/api/alerts/configs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(config)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.lowStockThreshold").value(10));
    }

    @Test
    void testGetAllConfigs() throws Exception {
        when(alertService.getAllConfigs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/alerts/configs"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetConfigByProductId() throws Exception {
        AlertConfig config = new AlertConfig();
        config.setProductId(1L);

        when(alertService.getConfigByProductId(1L)).thenReturn(config);

        mockMvc.perform(get("/api/alerts/configs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));
    }

    @Test
    void testMarkAsRead() throws Exception {
        mockMvc.perform(put("/api/alerts/1/read"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUnreadCount() throws Exception {
        when(alertService.getUnreadCount(1L)).thenReturn(5L);

        mockMvc.perform(get("/api/alerts/recipient/1/unread-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
