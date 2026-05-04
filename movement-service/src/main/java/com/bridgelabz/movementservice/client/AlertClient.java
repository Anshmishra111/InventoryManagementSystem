package com.bridgelabz.movementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "alert-service", url = "http://localhost:8086")
public interface AlertClient {
    @PostMapping("/api/alerts")
    Map<String, Object> createAlert(@RequestBody Map<String, Object> alert);
}
