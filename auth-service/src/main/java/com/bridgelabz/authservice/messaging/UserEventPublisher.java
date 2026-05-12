package com.bridgelabz.authservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public static final String EXCHANGE_NAME = "user.events";
    public static final String ROUTING_KEY_REGISTERED = "user.registered";

    public void publishUserRegistered(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_REGISTERED, event);
    }

    public static class UserRegisteredEvent {
        private Long userId;
        private String email;
        private String fullName;
        private String role;

        public UserRegisteredEvent() {}

        public UserRegisteredEvent(Long userId, String email, String fullName, String role) {
            this.userId = userId;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
