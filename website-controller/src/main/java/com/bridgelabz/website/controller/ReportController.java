package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ProductClient productClient;

    @GetMapping("/stock-summary")
    public Map<String, Object> getStockSummary() {
        List<Map<String, Object>> products = productClient.getAllProducts();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalProducts", products.size());
        
        // Total Inventory Value
        double totalValue = products.stream()
                .mapToDouble(p -> {
                    Object price = p.get("sellingPrice");
                    Object stock = p.get("currentStockLevel");
                    if (price != null && stock != null) {
                        return Double.parseDouble(price.toString()) * Double.parseDouble(stock.toString());
                    }
                    return 0.0;
                }).sum();
        summary.put("totalValue", totalValue);

        // Group by Category
        Map<String, Long> categoryDistribution = products.stream()
                .filter(p -> p.get("category") != null)
                .collect(Collectors.groupingBy(p -> p.get("category").toString(), Collectors.counting()));
        summary.put("categoryDistribution", categoryDistribution);

        // Low Stock Items
        List<Map<String, Object>> lowStockItems = products.stream()
                .filter(p -> {
                    Object stock = p.get("currentStockLevel");
                    return stock != null && Double.parseDouble(stock.toString()) <= 10;
                }).collect(Collectors.toList());
        summary.put("lowStockItems", lowStockItems);

        return summary;
    }
}
