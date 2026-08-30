package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_advice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private HealthCondition.ConditionType conditionType; // null for generic

    private String title;
    private String content;
    private boolean mustRead = false;
}