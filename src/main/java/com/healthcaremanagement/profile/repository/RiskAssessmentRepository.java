package com.healthcaremanagement.profile.repository;

import com.healthcaremanagement.profile.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, String> {
    Optional<RiskAssessment> findFirstByPatientIdOrderByAssessedAtDesc(String patientId);
}