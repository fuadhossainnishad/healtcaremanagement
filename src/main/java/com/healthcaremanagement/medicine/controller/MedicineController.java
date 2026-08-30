@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<MedicineResponse>>> search(
        @RequestParam String query,
        @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicineResponse> result = medicineService.searchMedicines(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(result)));
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
        @CurrentUser String userId,
        @Valid @RequestBody OrderRequest request) {
        OrderResponse response = medicineService.placeOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order placed. Awaiting prescription approval."));
    }

    @PostMapping("/prescriptions/upload")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> uploadPrescription(
        @CurrentUser String userId,
        @RequestParam("file") MultipartFile file,
        @RequestParam Long orderId) {
        PrescriptionResponse response = medicineService.uploadPrescription(userId, orderId, file);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}