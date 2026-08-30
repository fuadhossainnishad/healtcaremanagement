package com.healthcaremanagement.testresult.repository;

import com.healthcaremanagement.testresult.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestReportRepository extends JpaRepository<TestReport, String> {
    Page<TestReport> findByPatientIdOrderByReportDateDesc(String patientId, Pageable pageable);
}