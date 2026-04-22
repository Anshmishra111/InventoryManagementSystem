package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.MovementClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementClient movementClient;

    @GetMapping("/history/{productId}")
    public List<Map<String, Object>> getMovementHistory(@PathVariable Long productId) {
        return movementClient.getMovementHistoryByProductId(productId);
    }
}
