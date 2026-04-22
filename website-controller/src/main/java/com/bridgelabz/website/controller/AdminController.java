package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/users")
@RequiredArgsConstructor
public class AdminController {

    private final AuthClient authClient;

    @GetMapping
    public List<Map<String, Object>> getUserData() {
        return authClient.getAllUsers();
    }
}
