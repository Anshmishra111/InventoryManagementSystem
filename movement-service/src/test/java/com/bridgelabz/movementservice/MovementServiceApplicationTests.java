package com.bridgelabz.movementservice;

import com.bridgelabz.movementservice.repository.InventoryRepository;
import com.bridgelabz.movementservice.repository.StockMovementRepository;
import com.bridgelabz.movementservice.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;


@SpringBootTest
class MovementServiceApplicationTests {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockBean
    private ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;


    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private StockMovementRepository stockMovementRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void contextLoads() {
    }

}


