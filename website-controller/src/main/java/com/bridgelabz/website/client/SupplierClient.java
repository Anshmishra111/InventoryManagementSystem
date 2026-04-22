package com.bridgelabz.website.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "supplier-service", url = "${services.supplier}")
public interface SupplierClient {

    @GetMapping("/api/suppliers")
    List<Map<String, Object>> getAllSuppliers();

}
