package com.healthcaremanagement.health.controller;

@RestController
@RequestMapping("/api/health/readings")
@RequiredArgsConstructor
public class HealthReadingController {

    private final HealthReadingService readingService;

    @PostMapping
    public ResponseEntity<ApiResponse<HealthReadingResponse>> addReading(
            @CurrentUser String patientId,
            @Valid @RequestBody HealthReadingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(readingService.addReading(patientId, request)));
    }

    @GetMapping("/condition/{conditionId}")
    public ResponseEntity<ApiResponse<PageResponse<HealthReadingResponse>>> getReadings(
            @PathVariable String conditionId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<HealthReadingResponse> page = readingService.getReadingsForCondition(conditionId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping("/condition/{conditionId}/trend")
    public ResponseEntity<ApiResponse<TrendDataResponse>> getTrend(
            @PathVariable String conditionId,
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(ApiResponse.success(readingService.getTrendData(conditionId, days)));
    }
}