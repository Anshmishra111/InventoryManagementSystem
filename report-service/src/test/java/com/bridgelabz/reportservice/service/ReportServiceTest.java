package com.bridgelabz.reportservice.service;

import com.bridgelabz.reportservice.entity.InventoryReport;
import com.bridgelabz.reportservice.repository.ReportRepository;
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
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private InventoryReport testReport;

    @BeforeEach
    void setUp() {
        testReport = new InventoryReport();
        testReport.setId(1L);
        testReport.setReportName("Monthly Inventory Summary");
        testReport.setDetails("{ \"totalItems\": 100 }");

    }

    @Test
    void testGenerateReport() {
        when(reportRepository.save(any(InventoryReport.class))).thenReturn(testReport);

        InventoryReport result = reportService.generateReport(testReport);

        assertNotNull(result);
        assertNotNull(result.getGeneratedAt());
        assertEquals("Monthly Inventory Summary", result.getReportName());

        verify(reportRepository, times(1)).save(testReport);
    }

    @Test
    void testGetReportById_Found() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));

        InventoryReport result = reportService.getReportById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteReport() {
        doNothing().when(reportRepository).deleteById(1L);

        reportService.deleteReport(1L);

        verify(reportRepository, times(1)).deleteById(1L);
    }
}
