package com.healthcaremanagement.health.service;

@Service
@RequiredArgsConstructor
public class HealthSummaryServiceImpl implements HealthSummaryService {

    private final HealthConditionService conditionService;
    private final HealthReadingService readingService;
    private final HealthNoteService noteService;
    private final HealthAdviceService adviceService;
    private final TestResultService testResultService;

    @Override
    @Cacheable(value = "healthSummary", key = "#patientId")
    public HealthSummaryResponse getHealthSummary(String patientId) {
        // Combine data from various services
        // Build summary of conditions with latest readings and status
        List<HealthConditionResponse> conditions = conditionService.getConditionsForPatient(patientId);
        List<HealthSummaryResponse.ConditionSummary> conditionSummaries = conditions.stream()
                .map(cond -> {
                    HealthReadingResponse latest = readingService.getLatestReading(cond.getId());
                    return HealthSummaryResponse.ConditionSummary.builder()
                            .conditionId(cond.getId())
                            .type(cond.getType())
                            .status(determineStatus(latest)) // compute based on thresholds
                            .lastReading(latest != null ? latest.getDisplayValue() : null)
                            .lastReadingDate(latest != null ? latest.getRecordedAt() : null)
                            .build();
                })
                .collect(Collectors.toList());

        List<HealthAdviceResponse> mustReadAdvice = adviceService.getMustReadAdvice();
        List<HealthNoteResponse> recentNotes = noteService.getRecentNotes(patientId, 3);
        List<TestResultResponse> recentResults = testResultService.getTestResults(patientId).stream()
                .limit(5).collect(Collectors.toList());

        return HealthSummaryResponse.builder()
                .conditions(conditionSummaries)
                .mustReadAdvice(mustReadAdvice)
                .recentNotes(recentNotes)
                .recentResults(recentResults)
                .build();
    }
}