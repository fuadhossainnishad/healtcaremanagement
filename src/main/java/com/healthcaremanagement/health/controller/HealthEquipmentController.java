package com.healthcaremanagement.health.controller;

@RestController
@RequestMapping("/api/health/equipment")
@RequiredArgsConstructor
public class HealthEquipmentController {

    private final HealthEquipmentService equipmentService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<EquipmentResponse>> requestEquipment(
            @CurrentUser String patientId,
            @Valid @RequestBody EquipmentRequestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(equipmentService.requestEquipment(patientId, request)));
    }
}