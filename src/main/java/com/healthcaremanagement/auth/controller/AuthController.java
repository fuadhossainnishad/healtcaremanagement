package com.healthcaremanagement.auth.controller;

import com.healthcaremanagement.auth.dto.*;
import com.healthcaremanagement.auth.service.AuthService;
import com.healthcaremanagement.common.response.ApiResponse;
import com.healthcaremanagement.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Registration initiated. Please verify OTP."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody OTPVerificationRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Phone verified successfully"));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody ResendOTPRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(ApiResponse.success(null, "OTP resent"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "OTP sent to your registered phone"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @CurrentUser String userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PutMapping("/onboarding")
    public ResponseEntity<ApiResponse<Void>> updateOnboarding(
            @CurrentUser String userId,
            @Valid @RequestBody OnboardingRequest request) {
        authService.updateOnboarding(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Onboarding updated"));
    }
}