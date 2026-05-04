package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.MovementClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementClient movementClient;

    @GetMapping
    public List<Map<String, Object>> getAllMovements() {
        return movementClient.getAllMovements();
    }

    @GetMapping("/inventory")
    public List<Map<String, Object>> getInventory() {
        return movementClient.getAllInventory();
    }

    @PostMapping
    public Map<String, Object> recordMovement(@RequestBody Map<String, Object> movement) {
        return movementClient.recordMovement(movement);
    }
}
