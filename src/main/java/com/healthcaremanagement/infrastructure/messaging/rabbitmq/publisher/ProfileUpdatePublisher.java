package com.healthcaremanagement.infrastructure.messaging.rabbitmq.publisher;

import com.healthcaremanagement.profile.event.ProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.profile}")
    private String profileExchange;

    @Value("${rabbitmq.routing.profile}")
    private String profileRoutingKey;

    public void publishProfileUpdated(String patientId) {
        ProfileUpdatedEvent event = new ProfileUpdatedEvent(patientId, LocalDateTime.now().toString());
        rabbitTemplate.convertAndSend(profileExchange, profileRoutingKey, event);
        log.info("Profile updated event published for patient {}", patientId);
    }
}