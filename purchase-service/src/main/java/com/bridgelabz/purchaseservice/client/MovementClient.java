package com.bridgelabz.purchaseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "movement-service", url = "${MOVEMENT_SERVICE_URL:http://localhost:8086}")
public interface MovementClient {
    @PostMapping("/api/movements")
    Map<String, Object> recordMovement(@RequestBody Map<String, Object> movement);
}
