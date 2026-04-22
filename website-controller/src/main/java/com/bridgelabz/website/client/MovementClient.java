package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "movement-service", url = "${services.movement}")
public interface MovementClient {

    @GetMapping("/api/movements/history/product/{productId}")
    List<Map<String, Object>> getMovementHistoryByProductId(@PathVariable("productId") Long productId);

}
