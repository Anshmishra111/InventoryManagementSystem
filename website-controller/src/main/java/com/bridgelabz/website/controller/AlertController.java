package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.AlertClient;
import com.bridgelabz.website.client.MovementClient;
import com.bridgelabz.website.client.ProductClient;
import com.bridgelabz.website.client.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertClient alertClient;
    private final ProductClient productClient;
    private final MovementClient movementClient;
    private final WarehouseClient warehouseClient;

    @GetMapping
    public List<Map<String, Object>> getActiveAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        // 1. GLOBAL ALERTS (Total Product Stock)
        List<Map<String, Object>> products = productClient.getAllProducts();
        Map<Long, Map<String, Object>> productMap = products.stream()
                .collect(Collectors.toMap(p -> Long.parseLong(p.get("id").toString()), p -> p));

        alerts.addAll(products.stream()
                .filter(p -> {
                    Object stock = p.get("currentStockLevel");
                    Object reorder = p.get("reorderLevel");
                    if (stock == null) return false;
                    double current = Double.parseDouble(stock.toString());
                    double threshold = (reorder != null) ? Double.parseDouble(reorder.toString()) : 10;
                    return current <= threshold;
                })
                .map(p -> {
                    Map<String, Object> a = new HashMap<>(p);
                    Long pid = Long.parseLong(p.get("id").toString());
                    String pName = p.get("name") != null ? p.get("name").toString() : "Product #" + pid;
                    a.put("productId", pid);
                    a.put("alertMessage", "Low Stock: Only " + p.get("currentStockLevel") + " units of " + pName + " remaining globally!");
                    a.put("type", "CRITICAL");
                    a.put("createdAt", System.currentTimeMillis());
                    return a;
                })
                .collect(Collectors.toList()));

        // 2. WAREHOUSE ALERTS (Low stock in specific locations)
        List<Map<String, Object>> warehouses = warehouseClient.getAllWarehouses();
        Map<Long, String> warehouseNames = warehouses.stream()
                .collect(Collectors.toMap(w -> Long.parseLong(w.get("id").toString()), w -> w.get("name").toString()));

        List<Map<String, Object>> inventoryRecords = movementClient.getAllInventory();
        alerts.addAll(inventoryRecords.stream()
                .filter(i -> {
                    Object qty = i.get("quantity");
                    if (qty == null) return false;
                    double current = Double.parseDouble(qty.toString());
                    
                    Long pid = Long.parseLong(i.get("productId").toString());
                    Map<String, Object> product = productMap.get(pid);
                    double threshold = 10;
                    if (product != null && product.get("reorderLevel") != null) {
                        threshold = Double.parseDouble(product.get("reorderLevel").toString());
                    }
                    
                    return current <= threshold;
                })
                .map(i -> {
                    Map<String, Object> a = new HashMap<>();
                    Long pid = Long.parseLong(i.get("productId").toString());
                    Long wid = Long.parseLong(i.get("warehouseId").toString());
                    String pName = productMap.containsKey(pid) ? productMap.get(pid).get("name").toString() : "Product #" + pid;
                    String wName = warehouseNames.getOrDefault(wid, "Warehouse #" + wid);
                    
                    a.put("productId", pid);
                    a.put("warehouseId", wid);
                    a.put("currentStock", i.get("quantity"));
                    a.put("alertMessage", "Warehouse Alert: " + pName + " has only " + i.get("quantity") + " units left at " + wName + "!");
                    a.put("type", "WARNING");
                    a.put("createdAt", System.currentTimeMillis());
                    return a;
                })
                .collect(Collectors.toList()));

        return alerts;
    }
}
