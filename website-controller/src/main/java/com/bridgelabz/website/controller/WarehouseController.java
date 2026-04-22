package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseClient warehouseClient;

    @GetMapping
    public List<Map<String, Object>> getWarehouseData() {
        return warehouseClient.getAllWarehouses();
    }
}
