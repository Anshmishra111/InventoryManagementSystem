package com.bridgelabz.alertservice;

import com.bridgelabz.alertservice.repository.AlertConfigRepository;
import com.bridgelabz.alertservice.repository.AlertHistoryRepository;
import com.bridgelabz.alertservice.repository.AlertRepository;
import com.bridgelabz.alertservice.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@SpringBootTest
class AlertServiceApplicationTests {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private AlertConfigRepository alertConfigRepository;

    @MockBean
    private AlertHistoryRepository alertHistoryRepository;

    @MockBean
    private AlertRepository alertRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    void contextLoads() {
    }

}


