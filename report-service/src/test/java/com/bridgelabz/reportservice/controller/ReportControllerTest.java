package com.bridgelabz.reportservice.controller;

import com.bridgelabz.reportservice.entity.InventoryReport;
import com.bridgelabz.reportservice.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.reportservice.security.JwtFilter;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGenerateReport() throws Exception {
        InventoryReport report = new InventoryReport();
        report.setReportName("Daily Report");

        when(reportService.generateReport(any(InventoryReport.class))).thenReturn(report);

        mockMvc.perform(post("/api/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(report)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportName").value("Daily Report"));
    }


    @Test
    void testGetReportById() throws Exception {
        InventoryReport report = new InventoryReport();
        report.setId(1L);
        report.setReportName("Daily Report");

        when(reportService.getReportById(1L)).thenReturn(report);

        mockMvc.perform(get("/api/reports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportName").value("Daily Report"));

    }

    @Test
    void testGetAllReports() throws Exception {
        when(reportService.getAllReports()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/reports"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
