package com.healthcaremanagement.health.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthReadingRequest {

    @NotNull
    private String conditionId;

    private LocalDateTime recordedAt; // if null, current time

    // BP
    private Integer systolic;
    private Integer diastolic;

    // Diabetes
    private Double glucoseFasting;
    private Double glucoseRandom;
    private Double hba1c;

    // CKD
    private Double egfr;
    private Double creatinine;

    // Asthma/COPD
    private Double peakFlow;
    private Double spirometry;

    // generic
    private Double value;

    private String source; // HOME, CLINIC, LAB
    private String notes;
}