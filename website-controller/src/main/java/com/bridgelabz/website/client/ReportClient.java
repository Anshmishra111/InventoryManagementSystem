package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@FeignClient(name = "report-service", url = "${services.report}")
public interface ReportClient {

    @GetMapping("/api/reports")
    List<Map<String, Object>> getAllReports();

    @GetMapping("/api/reports/health")
    List<Map<String, Object>> getInventoryHealth();

    @GetMapping("/api/reports/turnover")
    Map<String, Object> getInventoryTurnover();

    @GetMapping("/api/reports/top-moving")
    List<Map<String, Object>> getTopMovingProducts();

    @GetMapping("/api/reports/dead-stock")
    List<Map<String, Object>> getDeadStock(@RequestParam("days") int days);

    @GetMapping("/api/reports/utilization/{warehouseId}")
    Map<String, Object> getWarehouseUtilization(@PathVariable("warehouseId") Long warehouseId);
}
