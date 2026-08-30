package com.healthcaremanagement.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsResponse {
    private String fullName;
    private String bloodGroup;
    private Double height;
    private Double weight;
    private Double bmi;
    private String bmiCategory;
    private String address;
    private String area;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String ethnicity;
}