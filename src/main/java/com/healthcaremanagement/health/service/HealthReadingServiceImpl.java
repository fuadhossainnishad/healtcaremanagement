package com.healthcaremanagement.health.service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HealthReadingServiceImpl implements HealthReadingService {

    private final HealthReadingRepository readingRepository;
    private final HealthConditionRepository conditionRepository;
    private final HealthConditionService conditionService;
    private final RedisCacheService redisCacheService;
    private final RabbitTemplate rabbitTemplate; // for abnormal alerts

    @Override
    public HealthReadingResponse addReading(String patientId, HealthReadingRequest request) {
        HealthCondition condition = conditionRepository.findById(request.getConditionId())
                .orElseThrow(() -> new BusinessException("Condition not found"));

        // ensure patient owns the condition
        if (!condition.getPatientId().equals(patientId)) {
            throw new BusinessException("Unauthorized access to this condition");
        }

        HealthReading reading = HealthReading.builder()
                .condition(condition)
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .systolic(request.getSystolic())
                .diastolic(request.getDiastolic())
                .glucoseFasting(request.getGlucoseFasting())
                .glucoseRandom(request.getGlucoseRandom())
                .hba1c(request.getHba1c())
                .egfr(request.getEgfr())
                .creatinine(request.getCreatinine())
                .peakFlow(request.getPeakFlow())
                .spirometry(request.getSpirometry())
                .value(request.getValue())
                .source(request.getSource() != null ? HealthReading.ReadingSource.valueOf(request.getSource())
                        : HealthReading.ReadingSource.HOME)
                .notes(request.getNotes())
                .build();

        // Determine if abnormal based on condition type and thresholds (simplified)
        boolean abnormal = checkAbnormal(reading, condition.getType());
        reading.setAbnormal(abnormal);

        reading = readingRepository.save(reading);

        // Invalidate cached trend data for this condition
        redisCacheService.delete("health:trend:" + condition.getId());

        // Publish event if abnormal
        if (abnormal) {
            rabbitTemplate.convertAndSend("health.exchange", "health.abnormal", new AbnormalReadingEvent(reading));
        }

        return HealthReadingResponse.from(reading);
    }

    @Override
    @Cacheable(value = "health:trend", key = "#conditionId + '-' + #days")
    public TrendDataResponse getTrendData(String conditionId, int days) {
        // Fetch readings for last N days and prepare trend data
        // ...
    }
}