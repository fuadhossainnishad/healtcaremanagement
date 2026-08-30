package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private HealthCondition condition;

    private String doctorName;
    private String content;
    private LocalDateTime notedAt;
}