package com.healthcaremanagement.config;

import java.util.Queue;

import org.springframework.context.annotation.Bean;

public class RabbitMQConfig {
    @Bean
    public Queue otpQueue() {
        return new Queue("otp.queue", true);
    }

    @Bean
    public TopicExchange otpExchange() {
        return new TopicExchange("otp.exchange");
    }

    @Bean
    public Binding otpBinding() {
        return BindingBuilder.bind(otpQueue()).to(otpExchange()).with("otp.routing.key");
    }
}
