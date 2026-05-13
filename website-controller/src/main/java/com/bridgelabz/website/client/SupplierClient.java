package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "supplier-service", url = "${services.supplier}")
public interface SupplierClient {

    @GetMapping("/api/suppliers")
    List<Map<String, Object>> getAllSuppliers();

    @GetMapping("/api/suppliers/{id}")
    Map<String, Object> getSupplierById(@PathVariable("id") Long id);

    @PostMapping("/api/suppliers")
    Map<String, Object> addSupplier(@RequestBody Map<String, Object> supplier);

    @PutMapping("/api/suppliers/{id}")
    Map<String, Object> updateSupplier(@PathVariable("id") Long id, @RequestBody Map<String, Object> supplier);

    @DeleteMapping("/api/suppliers/{id}")
    void deactivateSupplier(@PathVariable("id") Long id);
}
