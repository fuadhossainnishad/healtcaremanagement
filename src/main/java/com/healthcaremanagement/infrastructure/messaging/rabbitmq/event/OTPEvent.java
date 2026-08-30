package com.healthcaremanagement.infrastructure.messaging.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPEvent implements Serializable {
    private String phoneNumber;
    private String otp;
}