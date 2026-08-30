package com.healthcaremanagement.profile.controller;

import com.healthcaremanagement.common.response.ApiResponse;
import com.healthcaremanagement.profile.dto.*;
import com.healthcaremanagement.profile.service.ProfileService;
import com.healthcaremanagement.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProfileSummaryResponse>> getProfileSummary(@CurrentUser String patientId) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getProfileSummary(patientId)));
    }

    @GetMapping("/personal")
    public ResponseEntity<ApiResponse<PersonalDetailsResponse>> getPersonalDetails(@CurrentUser String patientId) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getPersonalDetails(patientId)));
    }

    @PutMapping("/personal")
    public ResponseEntity<ApiResponse<PersonalDetailsResponse>> updatePersonalDetails(
            @CurrentUser String patientId,
            @Valid @RequestBody PersonalDetailsRequest request) {
        PersonalDetailsResponse updated = profileService.updatePersonalDetails(patientId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Personal details updated"));
    }

    @GetMapping("/risk-assessment")
    public ResponseEntity<ApiResponse<RiskAssessmentResponse>> getLatestRiskAssessment(@CurrentUser String patientId) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getLatestRiskAssessment(patientId)));
    }

    @PostMapping("/risk-assessment")
    public ResponseEntity<ApiResponse<RiskAssessmentResponse>> submitRiskAssessment(
            @CurrentUser String patientId,
            @Valid @RequestBody RiskAssessmentRequest request) {
        RiskAssessmentResponse response = profileService.submitRiskAssessment(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Risk assessment submitted"));
    }
}