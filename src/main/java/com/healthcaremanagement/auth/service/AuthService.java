package com.healthcaremanagement.auth.service;

import com.healthcaremanagement.auth.dto.*;
import com.healthcaremanagement.auth.entity.AuthEntity;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    void verifyOtp(OTPVerificationRequest request);
    void resendOtp(ResendOTPRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    void changePassword(String userId, ChangePasswordRequest request);
    void updateOnboarding(String userId, OnboardingRequest request);
    AuthEntity getCurrentUser(String userId);
}