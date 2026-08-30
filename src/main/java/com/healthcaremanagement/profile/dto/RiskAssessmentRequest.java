package com.healthcaremanagement.profile.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessmentRequest {
    // Demographics
    @NotNull @Min(18) @Max(120)
    private Integer age;
    @NotBlank
    private String sex;
    private String ethnicity;
    private Double townsendScore;

    // Lifestyle
    private String smokingStatus;
    @Min(50) @Max(300)
    private Double heightCm;
    @Min(10) @Max(500)
    private Double weightKg;

    // BP & Cholesterol
    @NotNull
    private Integer systolicBp;
    private Boolean onBpTreatment;
    private Double totalCholesterol;
    private Double hdlCholesterol;

    // Medical conditions
    private Boolean type1Diabetes;
    private Boolean type2Diabetes;
    private Boolean atrialFibrillation;
    private Boolean chronicKidneyDisease;
    private Boolean rheumatoidArthritis;
    private Boolean systemicLupus;
    private Boolean migraine;
    private Boolean severeMentalIllness;
    private Boolean atypicalAntipsychoticUse;
    private Boolean steroidTabletsLongTerm;
    private Boolean erectileDysfunction;
    private Boolean familyHistoryCvd;
    private Boolean previousCvd;
}