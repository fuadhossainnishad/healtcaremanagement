package com.healthcaremanagement.health.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.healthcaremanagement.health.entity.HealthMedication;

public interface HealthMedicationRepository extends JpaRepository<HealthMedication, String> {
    @Query("SELECT m FROM HealthMedication m JOIN m.condition c WHERE c.patientId = :patientId AND m.active = true")
    List<HealthMedication> findActiveMedicationsForPatient(@Param("patientId") String patientId);
}
