package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "report-service", url = "${services.report}")
public interface ReportClient {

    @GetMapping("/api/reports")
    List<Map<String, Object>> getAllReports();

}
