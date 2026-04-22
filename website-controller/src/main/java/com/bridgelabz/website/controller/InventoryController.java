package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final ProductClient productClient;

    @GetMapping
    public List<Map<String, Object>> getInventoryData() {
        return productClient.getAllProducts();
    }
}
