package com.bridgelabz.reportservice.service;

import com.bridgelabz.reportservice.entity.InventoryReport;
import java.util.List;

public interface ReportService {
    InventoryReport generateReport(InventoryReport report);
    List<InventoryReport> getAllReports();
    InventoryReport getReportById(Long id);
    void deleteReport(Long id);
}
