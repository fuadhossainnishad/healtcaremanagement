package com.healthcaremanagement.profile.service;

import com.healthcaremanagement.auth.entity.AuthEntity;
import com.healthcaremanagement.auth.repository.AuthRepository;
import com.healthcaremanagement.common.exception.BusinessException;
import com.healthcaremanagement.health.entity.HealthCondition;
import com.healthcaremanagement.health.entity.HealthMedication;
import com.healthcaremanagement.health.repository.HealthConditionRepository;
import com.healthcaremanagement.health.repository.HealthMedicationRepository;
import com.healthcaremanagement.infrastructure.caching.RedisCacheService;
import com.healthcaremanagement.infrastructure.messaging.rabbitmq.publisher.ProfileUpdatePublisher;
import com.healthcaremanagement.patient.entity.PatientEntity;
import com.healthcaremanagement.patient.repository.PatientRepository;
import com.healthcaremanagement.profile.dto.*;
import com.healthcaremanagement.profile.entity.RiskAssessment;
import com.healthcaremanagement.profile.mapper.ProfileMapper;
import com.healthcaremanagement.profile.repository.RiskAssessmentRepository;
import com.healthcaremanagement.testresult.entity.TestReport;
import com.healthcaremanagement.testresult.repository.TestReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final AuthRepository authRepository;
    private final PatientRepository patientRepository;
    private final HealthConditionRepository healthConditionRepository;
    private final HealthMedicationRepository healthMedicationRepository;
    private final TestReportRepository testReportRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final ProfileMapper mapper;
    private final RedisCacheService redisCacheService;
    private final ProfileUpdatePublisher profileUpdatePublisher;

    @Override
    @Cacheable(value = "profileSummary", key = "#patientId")
    public ProfileSummaryResponse getProfileSummary(String patientId) {
        // Fetch all required data
        AuthEntity auth = authRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("User not found"));
        PatientEntity patient = patientRepository.findByAuthId(patientId)
                .orElseThrow(() -> new BusinessException("Patient profile not found"));

        // Personal details
        PersonalDetailsResponse personal = buildPersonalDetails(auth, patient);

        // Medical history – use HealthCondition
        List<HealthCondition> conditions = healthConditionRepository.findByPatientIdAndActiveTrue(patientId);
        List<ProfileSummaryResponse.MedicalHistoryResponse> medicalHistory = conditions.stream()
                .map(c -> ProfileSummaryResponse.MedicalHistoryResponse.builder()
                        .condition(c.getType().name().replace('_', ' '))
                        .diagnosedDate(c.getDiagnosedDate() != null ? c.getDiagnosedDate().toString() : null)
                        .notes(c.getNotes())
                        .build())
                .collect(Collectors.toList());

        // Latest risk assessment
        RiskAssessment latestRisk = riskAssessmentRepository.findFirstByPatientIdOrderByAssessedAtDesc(patientId)
                .orElse(null);
        RiskAssessmentResponse riskResponse = latestRisk != null ? mapper.toRiskResponse(latestRisk) : null;

        // Document summary – count by type (placeholder – we'll use existing TestResult)
        List<TestReport> testReports = testReportRepository.findByPatientId(patientId);
        long totalDocs = testReports.size();
        long prescriptionCount = testReports.stream().filter(r -> "Prescription".equals(r.getTestType())).count();
        long testResultCount = testReports.stream().filter(r -> "Test Report".equals(r.getTestType())).count();
        long othersCount = totalDocs - prescriptionCount - testResultCount;
        DocumentSummaryResponse docSummary = DocumentSummaryResponse.builder()
                .total(totalDocs)
                .prescription(prescriptionCount)
                .testResult(testResultCount)
                .others(othersCount)
                .build();

        // Test history – recent test reports
        List<TestReport> recentTests = testReportRepository.findByPatientIdOrderByReportDateDesc(patientId)
                .stream().limit(5).collect(Collectors.toList());
        List<ProfileSummaryResponse.TestHistoryEntry> testHistory = recentTests.stream()
                .map(t -> ProfileSummaryResponse.TestHistoryEntry.builder()
                        .testType(t.getTestType())
                        .date(t.getReportDate().toString())
                        .status(t.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        // Medicines – from all active medications
        List<HealthMedication> medications = healthMedicationRepository.findActiveMedicationsForPatient(patientId);
        List<ProfileSummaryResponse.MedicineSummaryResponse> medicines = medications.stream()
                .map(m -> ProfileSummaryResponse.MedicineSummaryResponse.builder()
                        .name(m.getName())
                        .dosage(m.getDosage())
                        .frequency(m.getFrequency())
                        .build())
                .collect(Collectors.toList());

        return ProfileSummaryResponse.builder()
                .personalDetails(personal)
                .medicalHistory(medicalHistory)
                .latestRiskAssessment(riskResponse)
                .documentSummary(docSummary)
                .testHistory(testHistory)
                .medicines(medicines)
                .build();
    }

    @Override
    @Cacheable(value = "personalDetails", key = "#patientId")
    public PersonalDetailsResponse getPersonalDetails(String patientId) {
        AuthEntity auth = authRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("User not found"));
        PatientEntity patient = patientRepository.findByAuthId(patientId)
                .orElseThrow(() -> new BusinessException("Patient profile not found"));
        return buildPersonalDetails(auth, patient);
    }

    @Override
    @CacheEvict(value = {"personalDetails", "profileSummary"}, key = "#patientId")
    public PersonalDetailsResponse updatePersonalDetails(String patientId, PersonalDetailsRequest request) {
        AuthEntity auth = authRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("User not found"));
        PatientEntity patient = patientRepository.findByAuthId(patientId)
                .orElseThrow(() -> new BusinessException("Patient profile not found"));

        // Update auth fields (fullName, email, phone)
        if (request.getFullName() != null) auth.setFullName(request.getFullName());
        if (request.getEmail() != null) auth.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) auth.setPhoneNumber(request.getPhoneNumber());

        // Update patient fields
        if (request.getBloodGroup() != null) patient.setBloodGroup(request.getBloodGroup());
        if (request.getHeight() != null) patient.setHeight(request.getHeight());
        if (request.getWeight() != null) patient.setWeight(request.getWeight());
        if (request.getAddress() != null) patient.setAddress(request.getAddress());
        if (request.getArea() != null) patient.setArea(request.getArea());
        if (request.getGender() != null) {
            patient.setGender(PatientEntity.Gender.valueOf(request.getGender().toUpperCase()));
        }
        if (request.getDateOfBirth() != null) patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getEthnicity() != null) patient.setEthnicity(request.getEthnicity());

        authRepository.save(auth);
        patient = patientRepository.save(patient);

        // Publish event to notify other modules of profile change
        profileUpdatePublisher.publishProfileUpdated(patientId);

        return buildPersonalDetails(auth, patient);
    }

    @Override
    @CacheEvict(value = {"profileSummary", "latestRiskAssessment"}, key = "#patientId")
    public RiskAssessmentResponse submitRiskAssessment(String patientId, RiskAssessmentRequest request) {
        // Validate that patient exists
        authRepository.findById(patientId).orElseThrow(() -> new BusinessException("User not found"));

        // Calculate QRISK (placeholder – replace with actual algorithm)
        Double score = calculateQrisk(request);
        String category = categorizeRisk(score);

        RiskAssessment assessment = RiskAssessment.builder()
                .patientId(patientId)
                .age(request.getAge())
                .sex(request.getSex())
                .ethnicity(request.getEthnicity())
                .townsendScore(request.getTownsendScore())
                .smokingStatus(request.getSmokingStatus())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .systolicBp(request.getSystolicBp())
                .onBpTreatment(request.getOnBpTreatment())
                .totalCholesterol(request.getTotalCholesterol())
                .hdlCholesterol(request.getHdlCholesterol())
                .type1Diabetes(request.getType1Diabetes())
                .type2Diabetes(request.getType2Diabetes())
                .atrialFibrillation(request.getAtrialFibrillation())
                .chronicKidneyDisease(request.getChronicKidneyDisease())
                .rheumatoidArthritis(request.getRheumatoidArthritis())
                .systemicLupus(request.getSystemicLupus())
                .migraine(request.getMigraine())
                .severeMentalIllness(request.getSevereMentalIllness())
                .atypicalAntipsychoticUse(request.getAtypicalAntipsychoticUse())
                .steroidTabletsLongTerm(request.getSteroidTabletsLongTerm())
                .erectileDysfunction(request.getErectileDysfunction())
                .familyHistoryCvd(request.getFamilyHistoryCvd())
                .previousCvd(request.getPreviousCvd())
                .riskScore(score)
                .riskCategory(category)
                .assessedAt(LocalDateTime.now())
                .build();

        assessment = riskAssessmentRepository.save(assessment);
        // Invalidate any caches that depend on risk
        redisCacheService.delete("profileSummary::" + patientId);
        return mapper.toRiskResponse(assessment);
    }

    @Override
    @Cacheable(value = "latestRiskAssessment", key = "#patientId")
    public RiskAssessmentResponse getLatestRiskAssessment(String patientId) {
        RiskAssessment assessment = riskAssessmentRepository.findFirstByPatientIdOrderByAssessedAtDesc(patientId)
                .orElseThrow(() -> new BusinessException("No risk assessment found for this patient"));
        return mapper.toRiskResponse(assessment);
    }

    // --------------------- Helper methods ---------------------
    private PersonalDetailsResponse buildPersonalDetails(AuthEntity auth, PatientEntity patient) {
        return PersonalDetailsResponse.builder()
                .fullName(auth.getFullName())
                .bloodGroup(patient.getBloodGroup())
                .height(patient.getHeight())
                .weight(patient.getWeight())
                .bmi(patient.getBmi())
                .bmiCategory(patient.getBmiCategory())
                .address(patient.getAddress())
                .area(patient.getArea())
                .email(auth.getEmail())
                .phoneNumber(auth.getPhoneNumber())
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                .dateOfBirth(patient.getDateOfBirth())
                .ethnicity(patient.getEthnicity())
                .build();
    }

    private Double calculateQrisk(RiskAssessmentRequest request) {
        // Placeholder – implement actual QRISK algorithm
        // For demo, return a random-ish value based on age and BP
        double base = 0.01 * (request.getAge() - 18) + 0.001 * request.getSystolicBp();
        if (request.getType2Diabetes() != null && request.getType2Diabetes()) base += 0.05;
        if (request.getSmokingStatus() != null && "CURRENT".equalsIgnoreCase(request.getSmokingStatus())) base += 0.03;
        return Math.min(Math.max(base * 100, 0), 100);
    }

    private String categorizeRisk(Double score) {
        if (score < 10) return "LOW";
        if (score < 20) return "MODERATE";
        return "HIGH";
    }
}