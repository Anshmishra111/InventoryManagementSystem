package com.bridgelabz.alertservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "movement-service", url = "${MOVEMENT_SERVICE_URL:http://localhost:8086}")
public interface MovementClient {
    @GetMapping("/api/movements/inventory")
    List<Map<String, Object>> getAllInventory();
}
