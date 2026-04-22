package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.AlertClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertClient alertClient;

    @GetMapping
    public List<Map<String, Object>> getAlertData() {
        return alertClient.getAlertHistory();
    }
}
