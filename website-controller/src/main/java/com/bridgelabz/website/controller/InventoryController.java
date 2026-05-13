package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard/inventory")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "product-management-controller", description = "Operations for managing products via the Hub")
public class InventoryController {

    private final ProductClient productClient;
    private final WarehouseClient warehouseClient;
    private final MovementClient movementClient;
    private final AlertClient alertClient;
    private final ReportClient reportClient;

    // --- Product Management ---
    @GetMapping
    public List<Map<String, Object>> getAllProducts() {
        try {
            System.out.println("DEBUG [Hub]: Fetching all products from product-service...");
            List<Map<String, Object>> products = productClient.getAllProducts();
            if (products != null) {
                for (Map<String, Object> p : products) {
                    Long id = Long.valueOf(p.get("id").toString());
                    List<Map<String, Object>> inventory = movementClient.getInventoryByProductId(id);
                    int total = 0;
                    if (inventory != null) {
                        for (Map<String, Object> item : inventory) {
                            Object qty = item.get("quantity");
                            if (qty != null) {
                                total += Integer.parseInt(qty.toString());
                            }
                        }
                    }
                    p.put("currentStockLevel", total);
                }
            }
            return products;
        } catch (Exception e) {
            System.err.println("ERROR [Hub]: Failed to fetch products: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/products")
    public List<Map<String, Object>> viewProducts() {
        return getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Map<String, Object> viewProductDetail(@PathVariable Long id) {
        return productClient.getProductById(id);
    }

    @PostMapping
    public Map<String, Object> createProductRoot(@RequestBody Map<String, Object> product) {
        return productClient.createProduct(product);
    }

    @PostMapping("/products")
    public Map<String, Object> addProduct(@RequestBody Map<String, Object> product) {
        return productClient.createProduct(product);
    }

    @PutMapping("/{id}")
    public Map<String, Object> editProductRoot(@PathVariable Long id, @RequestBody Map<String, Object> product) {
        return productClient.updateProduct(id, product);
    }

    @PutMapping("/products/{id}")
    public Map<String, Object> editProduct(@PathVariable Long id, @RequestBody Map<String, Object> product) {
        return productClient.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deactivateProductRoot(@PathVariable Long id) {
        System.out.println("DEBUG [website-controller]: Request to deactivate product ID: " + id);
        productClient.deleteProduct(id);
    }

    @DeleteMapping("/products/{id}")
    public void deactivateProduct(@PathVariable Long id) {
        productClient.deleteProduct(id);
    }

    @GetMapping("/products/search")
    public List<Map<String, Object>> searchProducts(@RequestParam String query) {
        return productClient.searchProducts(query);
    }

    @GetMapping("/products/barcode/{barcode}")
    public Map<String, Object> scanBarcode(@PathVariable String barcode) {
        return productClient.getProductByBarcode(barcode);
    }

    // --- Warehouse & Stock ---
    @GetMapping("/warehouses")
    public List<Map<String, Object>> viewWarehouses() {
        List<Map<String, Object>> warehouses = warehouseClient.getAllWarehouses();
        for (Map<String, Object> w : warehouses) {
            Long id = Long.valueOf(w.get("id").toString());
            List<Map<String, Object>> inventory = movementClient.getInventoryByWarehouseId(id);
            w.put("inventory", inventory);
            
            // Recalculate total units based on actual inventory
            int total = 0;
            if (inventory != null) {
                for (Map<String, Object> item : inventory) {
                    Object qty = item.get("quantity");
                    if (qty != null) {
                        total += Integer.parseInt(qty.toString());
                    }
                }
            }
            w.put("usedCapacity", total);
        }
        return warehouses;
    }

    @GetMapping("/warehouses/{id}")
    public Map<String, Object> viewWarehouseDetail(@PathVariable Long id) {
        return warehouseClient.getWarehouseById(id);
    }

    @GetMapping("/stock-levels")
    public List<Map<String, Object>> viewStockLevels() {
        return movementClient.getAllInventory();
    }

    @PostMapping("/transfer")
    public Map<String, Object> transferStock(@RequestBody Map<String, Object> movement) {
        return movementClient.recordMovement(movement);
    }

    // --- Movements & History ---
    @GetMapping("/movements")
    public List<Map<String, Object>> viewMovements() {
        return movementClient.getAllMovements();
    }

    @GetMapping("/movements/inventory")
    public List<Map<String, Object>> viewMovementInventory() {
        return movementClient.getAllInventory();
    }

    // --- Alerts ---
    @GetMapping("/alerts")
    public List<Map<String, Object>> viewAlerts() {
        return alertClient.getAllAlerts();
    }

    @PutMapping("/alerts/{id}/read")
    public void markAlertRead(@PathVariable Long id) {
        alertClient.markAsRead(id);
    }

    @PutMapping("/alerts/{id}/acknowledge")
    public void acknowledgeAlert(@PathVariable Long id) {
        alertClient.acknowledge(id);
    }

    // --- Reports ---
    @GetMapping("/reports/low-stock")
    public List<Map<String, Object>> viewLowStockReport() {
        return reportClient.getInventoryHealth(); // Assuming this returns health/low stock
    }
}
