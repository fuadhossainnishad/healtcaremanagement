package com.healthcaremanagement.profile.service;

import com.healthcaremanagement.profile.dto.*;

public interface ProfileService {

    ProfileSummaryResponse getProfileSummary(String patientId);

    PersonalDetailsResponse getPersonalDetails(String patientId);
    PersonalDetailsResponse updatePersonalDetails(String patientId, PersonalDetailsRequest request);

    RiskAssessmentResponse submitRiskAssessment(String patientId, RiskAssessmentRequest request);
    RiskAssessmentResponse getLatestRiskAssessment(String patientId);

    // Additional methods for document upload, etc. could be added
}