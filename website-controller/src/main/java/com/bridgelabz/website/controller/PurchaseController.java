package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.PurchaseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseClient purchaseClient;

    @GetMapping
    public List<Map<String, Object>> getPurchaseData() {
        return purchaseClient.getAllPurchaseOrders();
    }
}
