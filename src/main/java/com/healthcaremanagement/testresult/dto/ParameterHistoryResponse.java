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
public class ParameterHistoryResponse {
    private String parameterName;
    private String unit;
    private String normalRangeDisplay;
    private List<HistoryPoint> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryPoint {
        private LocalDateTime reportDate;
        private Double value;
        private String status;
    }
}