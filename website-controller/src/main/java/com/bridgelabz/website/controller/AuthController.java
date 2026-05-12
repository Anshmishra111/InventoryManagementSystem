package com.bridgelabz.website.controller;

import com.bridgelabz.website.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthClient authClient;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        // Now correctly expecting 'email' and 'password' from the frontend
        return ResponseEntity.ok(authClient.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> registerRequest) {
        // Now correctly expecting 'fullName', 'email', 'password', 'role', and 'department'
        return ResponseEntity.ok(authClient.register(registerRequest));
    }
}
