package com.healthcaremanagement.patient.entity;

import com.healthcaremanagement.auth.entity.AuthEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;

    // Existing fields
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private String location;
    private String profileImageUrl;

    // New personal details fields
    private String bloodGroup;               // e.g., "O+"
    private Double height;                  // in cm
    private Double weight;                  // in kg
    private String address;
    private String ethnicity;               // e.g., "Asian", "White"
    private String area;                    // e.g., "Banasree"

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // BMI computed property – not stored, but derived
    public Double getBmi() {
        if (height == null || weight == null || height <= 0) return null;
        return weight / ((height / 100) * (height / 100));
    }

    public String getBmiCategory() {
        Double bmi = getBmi();
        if (bmi == null) return null;
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}