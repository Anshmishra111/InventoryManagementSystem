package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "auth-service", url = "${services.auth}")
public interface AuthClient {

    @GetMapping("/auth/users")
    List<Map<String, Object>> getAllUsers();

}
