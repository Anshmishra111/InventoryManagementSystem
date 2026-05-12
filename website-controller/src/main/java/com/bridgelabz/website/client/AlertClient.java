package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "alert-service", url = "${services.alert}")
public interface AlertClient {

    @GetMapping("/api/alerts")
    List<Map<String, Object>> getAllAlerts();

    @GetMapping("/api/alerts/recipient/{recipientId}")
    List<Map<String, Object>> getAlertsByRecipient(@PathVariable("recipientId") Long recipientId);

    @org.springframework.web.bind.annotation.PutMapping("/api/alerts/{id}/read")
    void markAsRead(@PathVariable("id") Long id);

    @org.springframework.web.bind.annotation.PutMapping("/api/alerts/{id}/acknowledge")
    void acknowledge(@PathVariable("id") Long id);

    @GetMapping("/api/alerts/recipient/{recipientId}/unread-count")
    Long getUnreadCount(@PathVariable("recipientId") Long recipientId);
}
