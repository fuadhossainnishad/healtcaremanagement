package com.healthcaremanagement.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPResponse {
    private String message;
    private boolean sent;
    private String destination; // masked email/phone
}