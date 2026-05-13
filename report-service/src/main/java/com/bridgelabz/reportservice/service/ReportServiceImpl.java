package com.bridgelabz.reportservice.service;

import com.bridgelabz.reportservice.entity.InventoryReport;
import com.bridgelabz.reportservice.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public InventoryReport generateReport(InventoryReport report) {
        report.setGeneratedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Override
    public List<InventoryReport> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public InventoryReport getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
