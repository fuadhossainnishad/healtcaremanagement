package com.healthcaremanagement.health.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcaremanagement.testresult.entity.TestReport;

public interface TestResultRepository extends JpaRepository<TestReport, String> {
    List<TestReport> findByPatientId(String patientId);

    List<TestReport> findByPatientIdOrderByReportDateDesc(String patientId);
}
