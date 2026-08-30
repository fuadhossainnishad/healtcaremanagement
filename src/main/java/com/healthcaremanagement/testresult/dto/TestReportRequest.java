package com.healthcaremanagement.testresult.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TestReportRequest {

    @NotBlank
    private String reportTitle;

    @NotBlank
    private String testType;          // e.g., "Blood test"

    private String labName;

    @NotNull
    private LocalDateTime reportDate;

    private String notes;             // e.g., "Function test"

    private String status;            // URGENT, NON_URGENT, NORMAL

    @NotNull
    private List<TestParameterRequest> parameters;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestParameterRequest {
    @NotBlank
    private String name;
    private String displayName;
    private Double value;
    private String unit;
    private Double normalRangeLow;
    private Double normalRangeHigh;
    private String status; // Normal, Abnormal, High, Low
    private String category;
}