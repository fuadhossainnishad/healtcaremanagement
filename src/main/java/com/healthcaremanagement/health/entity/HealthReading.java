package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_readings")
@Inheritance(strategy = InheritanceType.JOINED) // optional, but we'll keep single table with nullable fields
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthReading {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private HealthCondition condition;

    private LocalDateTime recordedAt;

    // Common fields – some may be null based on condition type
    private Double value;          // e.g., glucose, peak flow, creatinine
    private Integer systolic;      // for BP
    private Integer diastolic;     // for BP
    private Double glucoseFasting; // for diabetes
    private Double glucoseRandom;
    private Double hba1c;
    private Double peakFlow;       // for asthma/COPD
    private Double spirometry;     // for asthma/COPD
    private Double egfr;           // for CKD
    private Double creatinine;     // for CKD

    // Metadata
    @Enumerated(EnumType.STRING)
    private ReadingSource source;  // HOME, CLINIC, LAB

    private String notes;

    @Column(nullable = false)
    private boolean abnormal = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ReadingSource {
        HOME, CLINIC, LAB
    }
}