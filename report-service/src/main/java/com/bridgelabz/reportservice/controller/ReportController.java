package com.bridgelabz.reportservice.controller;

import com.bridgelabz.reportservice.entity.InventoryReport;
import com.bridgelabz.reportservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<InventoryReport> generateReport(@RequestBody InventoryReport report) {
        return ResponseEntity.ok(reportService.generateReport(report));
    }

    @GetMapping
    public ResponseEntity<List<InventoryReport>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryReport> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<List<Object>> getInventoryHealth() {
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }

    @GetMapping("/turnover")
    public ResponseEntity<Object> getInventoryTurnover() {
        return ResponseEntity.ok(new java.util.HashMap<>());
    }

    @GetMapping("/top-moving")
    public ResponseEntity<List<Object>> getTopMovingProducts() {
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }

    @GetMapping("/dead-stock")
    public ResponseEntity<List<Object>> getDeadStock(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }

    @GetMapping("/utilization/{warehouseId}")
    public ResponseEntity<Object> getWarehouseUtilization(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(new java.util.HashMap<>());
    }
}
