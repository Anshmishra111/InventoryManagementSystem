package com.bridgelabz.warehouseservice;

import com.bridgelabz.warehouseservice.repository.WarehouseRepository;
import com.bridgelabz.warehouseservice.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WarehouseServiceApplicationTests {

    @MockBean
    private WarehouseRepository warehouseRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void contextLoads() {
    }

}

