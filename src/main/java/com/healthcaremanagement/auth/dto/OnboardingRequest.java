package com.healthcaremanagement.auth.dto;

import com.healthcaremanagement.patient.entity.PatientEntity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingRequest {

    private Gender gender;          // step 1

    private LocalDate dateOfBirth;  // step 2

    private String location;        // step 3

    // The client sends which step is being completed (1,2,3)
    @NotNull(message = "Step is required")
    private Integer step;
}