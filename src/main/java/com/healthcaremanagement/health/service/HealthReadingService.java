package com.healthcaremanagement.health.service;

import com.healthcaremanagement.health.dto.HealthReadingRequest;
import com.healthcaremanagement.health.dto.HealthReadingResponse;
import com.healthcaremanagement.health.dto.TrendDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HealthReadingService {

    HealthReadingResponse addReading(String patientId, HealthReadingRequest request);

    Page<HealthReadingResponse> getReadingsForCondition(String conditionId, Pageable pageable);

    TrendDataResponse getTrendData(String conditionId, int days);

    void markAbnormalReadings(); // batch job could be scheduled
}