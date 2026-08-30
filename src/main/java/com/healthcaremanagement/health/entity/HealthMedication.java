package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private HealthCondition condition;

    private String name;
    private String dosage;          // e.g., "5mg"
    private String frequency;       // e.g., "Once daily"
    private String route;           // e.g., "Oral"
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active = true;
}