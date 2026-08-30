package com.healthcaremanagement.auth.service;

public interface OTPService {
    String generateAndSendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
    void invalidateOtp(String phoneNumber);
}