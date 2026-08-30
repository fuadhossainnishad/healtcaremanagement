package com.healthcaremanagement.auth.service;

import com.healthcaremanagement.auth.dto.*;
import com.healthcaremanagement.auth.entity.AuthEntity;
import com.healthcaremanagement.auth.entity.RoleEntity;
import com.healthcaremanagement.auth.exception.AuthException;
import com.healthcaremanagement.auth.repository.AuthRepository;
import com.healthcaremanagement.auth.repository.RoleRepository;
import com.healthcaremanagement.common.exception.BusinessException;
import com.healthcaremanagement.common.util.PasswordUtil;
import com.healthcaremanagement.patient.entity.PatientEntity;
import com.healthcaremanagement.patient.service.PatientService;
import com.healthcaremanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OTPService otpService;
    private final PatientService patientService;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match");
        }

        // Check if email or phone already exists
        if (authRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        if (authRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BusinessException("Phone number already registered");
        }

        // Build AuthEntity
        AuthEntity auth = AuthEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false) // not yet verified
                .phoneVerified(false)
                .onboardingCompleted(false)
                .onboardingStep(0)
                .build();

        // Assign default ROLE_PATIENT
        RoleEntity patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new BusinessException("Default role not found"));
        auth.setRoles(Set.of(patientRole));

        auth = authRepository.save(auth);

        // Create empty Patient record
        patientService.createPatientProfile(auth);

        // Send OTP for phone verification
        otpService.generateAndSendOtp(auth.getPhoneNumber());

        // Return token? Usually we don't log in until verified; we can return a message.
        return LoginResponse.builder()
                .message("Registration successful. Please verify your phone number with the OTP sent.")
                .build();
    }

    @Override
    @Transactional
    public void verifyOtp(OTPVerificationRequest request) {
        AuthEntity auth = authRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (auth.isPhoneVerified()) {
            throw new BusinessException("Phone already verified");
        }

        boolean valid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());
        if (!valid) {
            throw new BusinessException("Invalid OTP");
        }

        auth.setPhoneVerified(true);
        auth.setEnabled(true); // enable account
        authRepository.save(auth);

        // optionally log user in or generate token
    }

    @Override
    public void resendOtp(ResendOTPRequest request) {
        AuthEntity auth = authRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException("User not found"));
        if (auth.isPhoneVerified()) {
            throw new BusinessException("Phone already verified");
        }
        otpService.invalidateOtp(request.getPhoneNumber());
        otpService.generateAndSendOtp(request.getPhoneNumber());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Authenticate using email or phone
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword())
        );
        // After authentication, we need to fetch user by email/phone
        AuthEntity auth = authRepository.findByEmailOrPhone(request.getEmailOrPhone())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!auth.isEnabled()) {
            throw new BusinessException("Account not verified. Please verify your phone number.");
        }

        String token = jwtService.generateToken(auth);
        return LoginResponse.builder()
                .token(token)
                .fullName(auth.getFullName())
                .email(auth.getEmail())
                .phoneNumber(auth.getPhoneNumber())
                .onboardingCompleted(auth.isOnboardingCompleted())
                .onboardingStep(auth.getOnboardingStep())
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // Find user by email or phone
        AuthEntity auth = authRepository.findByEmailOrPhone(request.getEmailOrPhone())
                .orElseThrow(() -> new BusinessException("User not found"));

        // Send OTP to the registered phone (or email). We'll send to phone for simplicity.
        otpService.generateAndSendOtp(auth.getPhoneNumber());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Verify OTP
        AuthEntity auth = authRepository.findByEmailOrPhone(request.getEmailOrPhone())
                .orElseThrow(() -> new BusinessException("User not found"));

        boolean valid = otpService.verifyOtp(auth.getPhoneNumber(), request.getOtp());
        if (!valid) {
            throw new BusinessException("Invalid OTP");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match");
        }

        auth.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authRepository.save(auth);
        otpService.invalidateOtp(auth.getPhoneNumber()); // consume OTP
    }

    @Override
    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        AuthEntity auth = authRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), auth.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match");
        }
        auth.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authRepository.save(auth);
    }

    @Override
    @Transactional
    public void updateOnboarding(String userId, OnboardingRequest request) {
        AuthEntity auth = authRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        // Validate step progression
        int expectedStep = auth.getOnboardingStep() + 1;
        if (request.getStep() != expectedStep) {
            throw new BusinessException("Invalid step. Expected step " + expectedStep);
        }

        // Update patient profile
        PatientEntity patient = patientService.getPatientByAuthId(userId);
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            patient.setLocation(request.getLocation());
        }
        patientService.savePatient(patient);

        // Update AuthEntity onboarding state
        auth.setOnboardingStep(request.getStep());
        if (request.getStep() == 3) {
            auth.setOnboardingCompleted(true);
        }
        authRepository.save(auth);
    }

    @Override
    public AuthEntity getCurrentUser(String userId) {
        return authRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}