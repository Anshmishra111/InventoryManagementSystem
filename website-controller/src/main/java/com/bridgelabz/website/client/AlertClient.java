package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "alert-service", url = "${services.alert}")
public interface AlertClient {

    @GetMapping("/api/alerts/history")
    List<Map<String, Object>> getAlertHistory();

}
