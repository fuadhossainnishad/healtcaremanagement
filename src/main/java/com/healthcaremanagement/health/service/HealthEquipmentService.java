package com.healthcaremanagement.health.service;

@Service
@RequiredArgsConstructor
@Transactional
public class HealthEquipmentServiceImpl implements HealthEquipmentService {

    private final EquipmentRequestRepository requestRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public EquipmentResponse requestEquipment(String patientId, EquipmentRequestRequest request) {
        EquipmentRequest eq = EquipmentRequest.builder()
                .patientId(patientId)
                .equipmentName(request.getEquipmentName())
                .description(request.getDescription())
                .type(EquipmentRequest.RequestType.valueOf(request.getType()))
                .requestedAt(LocalDateTime.now())
                .status(EquipmentRequest.RequestStatus.PENDING)
                .build();
        eq = requestRepository.save(eq);
        // publish event for admin notification
        rabbitTemplate.convertAndSend("equipment.exchange", "equipment.requested", eq);
        return EquipmentResponse.from(eq);
    }
}