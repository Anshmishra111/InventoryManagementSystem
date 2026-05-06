package com.bridgelabz.productservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    public static final String QUEUE_NAME = "user.registered.queue";

    @RabbitListener(queues = QUEUE_NAME)
    public void handleUserRegistered(UserRegisteredEvent event) {
        System.out.println("Received User Registered Event in Product Service:");
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Email: " + event.getEmail());
        System.out.println("Full Name: " + event.getFullName());
        System.out.println("Role: " + event.getRole());
        
        // Here you can implement logic to sync user data, send emails, etc.
    }

    public static class UserRegisteredEvent {
        private Long userId;
        private String email;
        private String fullName;
        private String role;

        public UserRegisteredEvent() {}

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
