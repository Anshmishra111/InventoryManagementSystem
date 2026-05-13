package com.bridgelabz.purchaseservice;

import com.bridgelabz.purchaseservice.repository.PurchaseOrderRepository;
import com.bridgelabz.purchaseservice.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PurchaseServiceApplicationTests {

    @MockBean
    private PurchaseOrderRepository purchaseOrderRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void contextLoads() {
    }

}

