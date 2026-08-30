package com.healthcaremanagement.infrastructure.service;

public interface NotificationService {
    void sendSms(String phoneNumber, String message);
    void sendEmail(String email, String subject, String body);
}