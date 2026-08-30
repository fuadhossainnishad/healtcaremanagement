package com.healthcaremanagement.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId;

    // Demographics
    private Integer age;
    private String sex;                     // MALE/FEMALE
    private String ethnicity;
    private Double townsendScore;

    // Lifestyle
    private String smokingStatus;           // NEVER, EX, CURRENT
    private Double heightCm;
    private Double weightKg;

    // BP & Cholesterol
    private Integer systolicBp;
    private Boolean onBpTreatment;
    private Double totalCholesterol;
    private Double hdlCholesterol;

    // Medical conditions (boolean flags)
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
    private Boolean erectileDysfunction;    // only for men
    private Boolean familyHistoryCvd;       // < 60 years
    private Boolean previousCvd;            // disables QRISK

    // Result
    private Double riskScore;               // e.g., 12.5 (percentage)
    private String riskCategory;            // LOW, MODERATE, HIGH
    private LocalDateTime assessedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}