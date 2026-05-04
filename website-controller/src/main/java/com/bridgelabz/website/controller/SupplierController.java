package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.SupplierClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierClient supplierClient;

    @GetMapping
    public List<Map<String, Object>> getSupplierData() {
        return supplierClient.getAllSuppliers();
    }

    @PostMapping
    public Map<String, Object> addSupplier(@RequestBody Map<String, Object> supplier) {
        return supplierClient.addSupplier(supplier);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateSupplier(@PathVariable Long id, @RequestBody Map<String, Object> supplier) {
        return supplierClient.updateSupplier(id, supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierClient.deactivateSupplier(id);
        return ResponseEntity.ok().build();
    }
}
