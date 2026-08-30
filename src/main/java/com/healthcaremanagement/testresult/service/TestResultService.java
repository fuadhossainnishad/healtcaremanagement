package com.healthcaremanagement.testresult.service;

import com.healthcaremanagement.testresult.dto.TestReportRequest;
import com.healthcaremanagement.testresult.dto.TestReportResponse;
import com.healthcaremanagement.testresult.dto.ParameterHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestResultService {

    TestReportResponse uploadTestReport(String patientId, TestReportRequest request);

    TestReportResponse getTestReport(String reportId);

    Page<TestReportResponse> getTestReportsForPatient(String patientId, Pageable pageable);

    ParameterHistoryResponse getParameterHistory(String patientId, String parameterName, int months);
}