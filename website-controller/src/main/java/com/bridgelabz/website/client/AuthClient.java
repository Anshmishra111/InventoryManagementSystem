package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;

@FeignClient(name = "auth-service", url = "${services.auth}")
public interface AuthClient {

    @GetMapping("/auth/users")
    List<Map<String, Object>> getAllUsers();

    @PostMapping("/auth/login")
    Map<String, Object> login(@RequestBody Map<String, String> loginRequest);

    @PostMapping("/auth/register")
    Map<String, Object> register(@RequestBody Map<String, Object> registerRequest);

    @org.springframework.web.bind.annotation.PutMapping("/auth/users/{id}")
    Map<String, Object> updateUser(@org.springframework.web.bind.annotation.PathVariable("id") Long id, @RequestBody Map<String, Object> userData);

    @org.springframework.web.bind.annotation.DeleteMapping("/auth/users/{id}")
    void deleteUser(@org.springframework.web.bind.annotation.PathVariable("id") Long id);

}
