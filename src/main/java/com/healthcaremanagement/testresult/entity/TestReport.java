package com.healthcaremanagement.testresult.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "test_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId;

    private String reportTitle;        // e.g., "FBC Nov 15, 2025"
    private String testType;           // e.g., "Blood test", "Scan", "Urine", "Stool"
    private String labName;

    private LocalDateTime reportDate;  // when the sample was taken

    private String notes;              // e.g., "Function test"

    @Enumerated(EnumType.STRING)
    private ReportStatus status;       // URGENT, NON_URGENT, NORMAL

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestParameter> parameters;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    public enum ReportStatus {
        URGENT, NON_URGENT, NORMAL
    }
}