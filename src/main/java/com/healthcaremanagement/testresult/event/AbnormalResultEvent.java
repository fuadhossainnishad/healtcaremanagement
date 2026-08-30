package com.healthcaremanagement.testresult.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalResultEvent implements Serializable {
    private String patientId;
    private String reportTitle;
    private LocalDateTime reportDate;
    private List<AbnormalParameter> abnormalParameters;

    @Data
    @AllArgsConstructor
    public static class AbnormalParameter {
        private String name;
        private Double value;
        private String status;
    }
}