package com.healthcaremanagement.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentResponse {
    private String id;
    private Double riskScore;
    private String riskCategory;
    private LocalDateTime assessedAt;
}