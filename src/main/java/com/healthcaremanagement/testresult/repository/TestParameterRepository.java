package com.healthcaremanagement.testresult.repository;

import com.healthcaremanagement.testresult.entity.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TestParameterRepository extends JpaRepository<TestParameter, String> {

    @Query("SELECT p FROM TestParameter p JOIN p.report r WHERE r.patientId = :patientId AND p.name = :parameterName AND r.reportDate >= :since ORDER BY r.reportDate ASC")
    List<TestParameter> findByPatientIdAndNameAndReportDateAfter(String patientId, String parameterName, LocalDateTime since);
}