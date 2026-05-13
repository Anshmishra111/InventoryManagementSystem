package com.bridgelabz.alertservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.List;

@FeignClient(name = "warehouse-service", url = "${WAREHOUSE_SERVICE_URL:http://localhost:8083}")
public interface WarehouseClient {
    @GetMapping("/api/warehouses/{id}")
    Map<String, Object> getWarehouseById(@PathVariable("id") Long id);

    @GetMapping("/api/warehouses")
    List<Map<String, Object>> getAllWarehouses();
}
