package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "health_conditions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"patient_id", "type"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId; // link to AuthEntity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType type; // BLOOD_PRESSURE, DIABETES, CKD, ASTHMA, COPD, HEART_CONDITION

    private LocalDateTime diagnosedDate;

    private String notes; // e.g., "Stage 2"

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ConditionType {
        BLOOD_PRESSURE, DIABETES, CKD, ASTHMA, COPD, HEART_CONDITION
    }
}