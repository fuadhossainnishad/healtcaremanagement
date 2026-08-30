package com.healthcaremanagement.infrastructure.messaging.rabbitmq.consumer;

import com.healthcaremanagement.infrastructure.messaging.rabbitmq.event.OTPEvent;
import com.healthcaremanagement.infrastructure.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OTPConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.otp}")
    public void handleOtpEvent(OTPEvent event) {
        log.info("Received OTP event for {}", event.getPhoneNumber());
        // Send SMS (or email if we have email in event)
        notificationService.sendSms(event.getPhoneNumber(), 
            "Your OTP is: " + event.getOtp() + ". It expires in 5 minutes.");
    }
}