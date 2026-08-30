package com.healthcaremanagement.testresult.controller;

import com.healthcaremanagement.common.response.ApiResponse;
import com.healthcaremanagement.common.response.PageResponse;
import com.healthcaremanagement.security.CurrentUser;
import com.healthcaremanagement.testresult.dto.ParameterHistoryResponse;
import com.healthcaremanagement.testresult.dto.TestReportRequest;
import com.healthcaremanagement.testresult.dto.TestReportResponse;
import com.healthcaremanagement.testresult.service.TestResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health/test-results")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<TestReportResponse>> uploadTestReport(
            @CurrentUser String patientId,
            @Valid @RequestBody TestReportRequest request) {
        TestReportResponse response = testResultService.uploadTestReport(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Test report uploaded successfully"));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ApiResponse<TestReportResponse>> getTestReport(@PathVariable String reportId) {
        return ResponseEntity.ok(ApiResponse.success(testResultService.getTestReport(reportId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TestReportResponse>>> getTestReports(
            @CurrentUser String patientId,
            @PageableDefault(size = 10, sort = "reportDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TestReportResponse> page = testResultService.getTestReportsForPatient(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/parameter/history")
    public ResponseEntity<ApiResponse<ParameterHistoryResponse>> getParameterHistory(
            @CurrentUser String patientId,
            @RequestParam String parameterName,
            @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(ApiResponse.success(
                testResultService.getParameterHistory(patientId, parameterName, months)));
    }
}