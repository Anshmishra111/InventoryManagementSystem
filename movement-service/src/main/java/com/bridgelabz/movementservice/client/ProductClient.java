package com.bridgelabz.movementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "product-service", url = "${PRODUCT_SERVICE_URL:http://localhost:8082}")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    Map<String, Object> getProductById(@PathVariable("id") Long id);

    @PutMapping("/api/products/{id}/stock")
    Map<String, Object> updateStock(@PathVariable("id") Long id, @RequestParam("quantityChange") Integer quantityChange, @RequestParam(required = false, value = "newCostPrice") Double newCostPrice);
}
