package com.healthcaremanagement.testresult.service;

import com.healthcaremanagement.common.exception.BusinessException;
import com.healthcaremanagement.infrastructure.caching.RedisCacheService;
import com.healthcaremanagement.infrastructure.messaging.rabbitmq.publisher.AbnormalResultPublisher;
import com.healthcaremanagement.testresult.dto.*;
import com.healthcaremanagement.testresult.entity.TestParameter;
import com.healthcaremanagement.testresult.entity.TestReport;
import com.healthcaremanagement.testresult.mapper.TestResultMapper;
import com.healthcaremanagement.testresult.repository.TestParameterRepository;
import com.healthcaremanagement.testresult.repository.TestReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TestResultServiceImpl implements TestResultService {

    private final TestReportRepository reportRepository;
    private final TestParameterRepository parameterRepository;
    private final TestResultMapper mapper;
    private final RedisCacheService redisCacheService;
    private final AbnormalResultPublisher abnormalResultPublisher;

    @Override
    @CacheEvict(value = {"patientTestReports", "parameterHistory"}, key = "#patientId")
    public TestReportResponse uploadTestReport(String patientId, TestReportRequest request) {
        // Build report entity
        TestReport report = TestReport.builder()
                .patientId(patientId)
                .reportTitle(request.getReportTitle())
                .testType(request.getTestType())
                .labName(request.getLabName())
                .reportDate(request.getReportDate())
                .notes(request.getNotes())
                .status(TestReport.ReportStatus.valueOf(request.getStatus()))
                .build();

        // Build parameters
        List<TestParameter> parameters = request.getParameters().stream()
                .map(p -> TestParameter.builder()
                        .report(report)
                        .name(p.getName())
                        .displayName(p.getDisplayName())
                        .value(p.getValue())
                        .unit(p.getUnit())
                        .normalRangeLow(p.getNormalRangeLow())
                        .normalRangeHigh(p.getNormalRangeHigh())
                        .status(p.getStatus())
                        .category(p.getCategory())
                        .build())
                .collect(Collectors.toList());

        report.setParameters(parameters);
        report = reportRepository.save(report);

        // Publish event if any parameter is abnormal
        boolean hasAbnormal = parameters.stream().anyMatch(p -> !"Normal".equalsIgnoreCase(p.getStatus()));
        if (hasAbnormal) {
            abnormalResultPublisher.publishAbnormal(report);
        }

        return mapper.toResponse(report);
    }

    @Override
    @Cacheable(value = "testReport", key = "#reportId")
    public TestReportResponse getTestReport(String reportId) {
        TestReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException("Report not found"));
        return mapper.toResponse(report);
    }

    @Override
    @Cacheable(value = "patientTestReports", key = "#patientId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<TestReportResponse> getTestReportsForPatient(String patientId, Pageable pageable) {
        return reportRepository.findByPatientIdOrderByReportDateDesc(patientId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Cacheable(value = "parameterHistory", key = "#patientId + '-' + #parameterName + '-' + #months")
    public ParameterHistoryResponse getParameterHistory(String patientId, String parameterName, int months) {
        LocalDateTime since = LocalDateTime.now().minus(months, ChronoUnit.MONTHS);

        // Query all reports with this parameter for the patient in date range
        List<TestParameter> parameters = parameterRepository.findByPatientIdAndNameAndReportDateAfter(
                patientId, parameterName, since);

        if (parameters.isEmpty()) {
            throw new BusinessException("No data found for parameter: " + parameterName);
        }

        // Build history points
        List<ParameterHistoryResponse.HistoryPoint> history = parameters.stream()
                .map(p -> ParameterHistoryResponse.HistoryPoint.builder()
                        .reportDate(p.getReport().getReportDate())
                        .value(p.getValue())
                        .status(p.getStatus())
                        .build())
                .sorted((a, b) -> a.getReportDate().compareTo(b.getReportDate()))
                .collect(Collectors.toList());

        // Get reference info from first parameter
        TestParameter first = parameters.get(0);
        String rangeDisplay = first.getNormalRangeLow() + " – " + first.getNormalRangeHigh() + " " + first.getUnit();

        return ParameterHistoryResponse.builder()
                .parameterName(parameterName)
                .unit(first.getUnit())
                .normalRangeDisplay(rangeDisplay)
                .history(history)
                .build();
    }
}