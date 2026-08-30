package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String patientId;
    private String title;          // e.g., "Blood Test Results"
    private String fileUrl;        // stored in cloud/disk
    private String fileType;       // pdf, jpg, etc.
    private LocalDateTime testDate;
    private String notes;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    private boolean abnormal = false;
}