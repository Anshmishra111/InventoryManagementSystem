package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service", url = "${services.product}")
public interface ProductClient {

    @GetMapping("/api/products")
    List<Map<String, Object>> getAllProducts();

    @GetMapping("/api/products/{id}")
    Map<String, Object> getProductById(@PathVariable("id") Long id);

    @PostMapping("/api/products")
    Map<String, Object> createProduct(@RequestBody Map<String, Object> product);

    @PutMapping("/api/products/{id}")
    Map<String, Object> updateProduct(@PathVariable("id") Long id, @RequestBody Map<String, Object> product);

    @DeleteMapping("/api/products/{id}")
    void deleteProduct(@PathVariable("id") Long id);

    @GetMapping("/api/products/search")
    List<Map<String, Object>> searchProducts(@RequestParam("query") String query);

    @GetMapping("/api/products/barcode/{barcode}")
    Map<String, Object> getProductByBarcode(@PathVariable("barcode") String barcode);
}
