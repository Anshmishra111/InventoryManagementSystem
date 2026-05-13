package com.bridgelabz.productservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public org.springframework.amqp.core.Queue stockUpdateQueue() {
        return new org.springframework.amqp.core.Queue("inventory.stock.queue");
    }

    @Bean
    public org.springframework.amqp.core.Queue userRegisteredQueue() {
        return new org.springframework.amqp.core.Queue("user.registered.queue");
    }

    @Bean
    public org.springframework.amqp.core.TopicExchange inventoryExchange() {
        return new org.springframework.amqp.core.TopicExchange("inventory.exchange");
    }

    @Bean
    public org.springframework.amqp.core.Binding stockBinding(org.springframework.amqp.core.Queue stockUpdateQueue, org.springframework.amqp.core.TopicExchange inventoryExchange) {
        return org.springframework.amqp.core.BindingBuilder.bind(stockUpdateQueue).to(inventoryExchange).with("inventory.movement.key");
    }
}
