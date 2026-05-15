package com.bridgelabz.productservice;

import com.bridgelabz.productservice.repository.ProductRepository;
import com.bridgelabz.productservice.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@SpringBootTest
class ProductServiceApplicationTests {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void contextLoads() {
    }

}


