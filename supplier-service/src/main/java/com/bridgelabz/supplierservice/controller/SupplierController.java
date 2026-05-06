package com.bridgelabz.supplierservice.controller;

import com.bridgelabz.supplierservice.entity.Supplier;
import com.bridgelabz.supplierservice.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.addSupplier(supplier));
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllActiveSuppliers() {
        return ResponseEntity.ok(supplierService.getAllActiveSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateSupplier(@PathVariable Long id) {
        supplierService.deactivateSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
