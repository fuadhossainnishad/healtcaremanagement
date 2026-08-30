package com.healthcaremanagement.health.controller;

@RestController
@RequestMapping("/api/health/conditions")
@RequiredArgsConstructor
public class HealthConditionController {

    private final HealthConditionService conditionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HealthConditionResponse>>> getConditions(@CurrentUser String patientId) {
        return ResponseEntity.ok(ApiResponse.success(conditionService.getConditionsForPatient(patientId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HealthConditionResponse>> addCondition(
            @CurrentUser String patientId,
            @Valid @RequestBody HealthConditionRequest request) {
        // ensure patientId from token matches
        HealthConditionResponse response = conditionService.addCondition(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}