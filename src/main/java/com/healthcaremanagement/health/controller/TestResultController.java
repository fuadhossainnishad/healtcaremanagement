package com.healthcaremanagement.health.controller;

@RestController
@RequestMapping("/api/health/test-results")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TestResultResponse>> uploadTestResult(
            @CurrentUser String patientId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") @Valid TestResultUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(testResultService.uploadTestResult(patientId, file, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestResultResponse>>> getTestResults(@CurrentUser String patientId) {
        return ResponseEntity.ok(ApiResponse.success(testResultService.getTestResults(patientId)));
    }
}