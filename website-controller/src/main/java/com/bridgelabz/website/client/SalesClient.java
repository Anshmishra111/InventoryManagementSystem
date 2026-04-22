package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "sales-service", url = "${services.sales}")
public interface SalesClient {

    @GetMapping("/api/sales/orders")
    List<Map<String, Object>> getAllOrders();

    @GetMapping("/api/sales/customers")
    List<Map<String, Object>> getAllCustomers();

}
