package com.bridgelabz.salesservice.controller;

import com.bridgelabz.salesservice.entity.Customer;
import com.bridgelabz.salesservice.entity.SalesOrder;
import com.bridgelabz.salesservice.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(salesService.addCustomer(customer));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(salesService.getAllCustomers());
    }

    @PostMapping("/orders")
    public ResponseEntity<SalesOrder> placeOrder(@RequestBody SalesOrder order) {
        return ResponseEntity.ok(salesService.placeOrder(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<SalesOrder>> getAllOrders() {
        return ResponseEntity.ok(salesService.getAllOrders());
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<List<SalesOrder>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(salesService.getOrdersByCustomerId(customerId));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<SalesOrder> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(salesService.updateStatus(id, status));
    }
}
