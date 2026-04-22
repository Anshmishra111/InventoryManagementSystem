package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "warehouse-service", url = "${services.warehouse}")
public interface WarehouseClient {

    @GetMapping("/api/warehouses")
    List<Map<String, Object>> getAllWarehouses();

}
