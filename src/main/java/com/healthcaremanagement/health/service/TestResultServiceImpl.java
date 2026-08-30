package com.healthcaremanagement.health.service;

@Service
@RequiredArgsConstructor
@Transactional
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository resultRepository;
    private final FileStorageService fileStorageService; // custom service for file upload

    @Override
    public TestResultResponse uploadTestResult(String patientId, MultipartFile file, TestResultUploadRequest request) {
        String fileUrl = fileStorageService.storeFile(file);
        TestResult result = TestResult.builder()
                .patientId(patientId)
                .title(request.getTitle())
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .testDate(request.getTestDate())
                .notes(request.getNotes())
                .abnormal(request.isAbnormal())
                .build();
        result = resultRepository.save(result);
        // Invalidate patient's health summary cache
        redisCacheService.delete("health:summary:" + patientId);
        return TestResultResponse.from(result);
    }

    @Override
    @Cacheable(value = "patientTestResults", key = "#patientId")
    public List<TestResultResponse> getTestResults(String patientId) {
        // ...
    }
}