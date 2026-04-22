package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.ReportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportClient reportClient;

    @GetMapping
    public List<Map<String, Object>> getReportData() {
        return reportClient.getAllReports();
    }
}
