package com.healthcaremanagement.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSummaryResponse {
    private PersonalDetailsResponse personalDetails;
    private List<MedicalHistoryResponse> medicalHistory;
    private RiskAssessmentResponse latestRiskAssessment;
    private DocumentSummaryResponse documentSummary;
    private List<TestHistoryEntry> testHistory;
    private List<MedicineSummaryResponse> medicines;

    @Data
    @Builder
    public static class MedicalHistoryResponse {
        private String condition;
        private String diagnosedDate;
        private String notes;
    }

    @Data
    @Builder
    public static class TestHistoryEntry {
        private String testType;
        private String date;
        private String status; // e.g., "Normal", "Abnormal"
    }

    @Data
    @Builder
    public static class MedicineSummaryResponse {
        private String name;
        private String dosage;
        private String frequency;
    }
}