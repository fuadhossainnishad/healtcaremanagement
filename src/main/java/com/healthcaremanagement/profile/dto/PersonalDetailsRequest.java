package com.healthcaremanagement.profile.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsRequest {

    private String fullName;
    private String bloodGroup;
    @Min(50) @Max(300)
    private Double height;   // cm
    @Min(10) @Max(500)
    private Double weight;   // kg
    private String address;
    private String area;
    private String email;
    private String phoneNumber;
    private String gender;   // MALE, FEMALE, OTHER
    private LocalDate dateOfBirth;
    private String ethnicity;
}