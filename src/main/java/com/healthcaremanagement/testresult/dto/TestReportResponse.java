package com.healthcaremanagement.testresult.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReportResponse {
    private String id;
    private String reportTitle;
    private String testType;
    private String labName;
    private LocalDateTime reportDate;
    private String notes;
    private String status;
    private LocalDateTime uploadedAt;
    private List<TestParameterResponse> parameters;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestParameterResponse {
    private String id;
    private String name;
    private String displayName;
    private Double value;
    private String unit;
    private Double normalRangeLow;
    private Double normalRangeHigh;
    private String status;
    private String category;
}