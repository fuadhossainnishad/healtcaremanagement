package com.healthcaremanagement.health.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthSummaryResponse {
    private List<ConditionSummary> conditions;
    private List<HealthAdviceResponse> mustReadAdvice;
    private List<HealthNoteResponse> recentNotes;
    private List<TestResultResponse> recentResults;

    @Data
    @Builder
    public static class ConditionSummary {
        private String conditionId;
        private String type;
        private String status;   // e.g., "Stable", "Over due", "Review"
        private String lastReading;
        private String lastReadingDate;
    }
}