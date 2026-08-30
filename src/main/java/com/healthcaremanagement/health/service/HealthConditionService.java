package com.healthcaremanagement.health.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthConditionServiceImpl implements HealthConditionService {

    private final HealthConditionRepository conditionRepository;

    @Override
    @Cacheable(value = "patientConditions", key = "#patientId")
    public List<HealthConditionResponse> getConditionsForPatient(String patientId) {
        List<HealthCondition> conditions = conditionRepository.findByPatientIdAndActiveTrue(patientId);
        return conditions.stream().map(HealthConditionResponse::from).collect(Collectors.toList());
    }

    // Add condition, update status, etc.
}