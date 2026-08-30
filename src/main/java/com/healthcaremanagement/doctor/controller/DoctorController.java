package com.healthcaremanagement.doctor.controller;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<DoctorResponse>>> searchDoctors(
        @RequestParam(required = false) String specialty,
        @RequestParam(required = false) String location,
        @PageableDefault(size = 10) Pageable pageable) {
        Page<DoctorResponse> doctors = doctorService.searchDoctors(specialty, location, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(doctors)));
    }

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<ApiResponse<List<AvailabilitySlot>>> getAvailability(
        @PathVariable String doctorId,
        @RequestParam LocalDate date) {
        List<AvailabilitySlot> slots = doctorService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(ApiResponse.success(slots));
    }

    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
        @CurrentUser String userId,
        @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Appointment booked. Confirmation sent."));
    }
}