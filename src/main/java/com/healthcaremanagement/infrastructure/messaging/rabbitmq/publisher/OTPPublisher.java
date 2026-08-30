package com.healthcaremanagement.infrastructure.messaging.rabbitmq.publisher;

import com.healthcaremanagement.infrastructure.messaging.rabbitmq.event.OTPEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.otp}")
    private String otpExchange;

    @Value("${rabbitmq.routing.otp}")
    private String otpRoutingKey;

    public void publishOtp(String phoneNumber, String otp) {
        OTPEvent event = new OTPEvent(phoneNumber, otp);
        rabbitTemplate.convertAndSend(otpExchange, otpRoutingKey, event);
        log.info("OTP event published for {}", phoneNumber);
    }
}