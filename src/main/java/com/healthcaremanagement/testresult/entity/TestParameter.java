package com.healthcaremanagement.testresult.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_parameters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private TestReport report;

    private String name;              // e.g., "WBC", "RBC", "Hemoglobin"
    private String displayName;       // for UI, optional
    private Double value;
    private String unit;              // e.g., "x10³/L", "g/dL", "mIU/L"
    private Double normalRangeLow;
    private Double normalRangeHigh;
    private String status;            // "Normal", "Abnormal", "High", "Low"

    private String category;          // e.g., "Blood count", "Thyroid", "Diabetes"
}