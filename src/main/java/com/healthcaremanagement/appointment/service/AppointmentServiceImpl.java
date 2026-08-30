package com.healthcaremanagement.appointment.service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final RedisCacheService redisCacheService;
    private final NotificationPublisher notificationPublisher;

    @Override
    @Cacheable(value = "doctor_availability", key = "#doctorId + '-' + #date")
    public List<AvailabilitySlot> getAvailableSlots(String doctorId, LocalDate date) {
        // Query DB for available slots, return DTOs
    }

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(String userId, AppointmentRequest request) {
        // 1. Lock the slot in DB (optimistic/pessimistic) to prevent double booking
        // 2. Create appointment record
        // 3. Invalidate cache for that date
        // 4. Publish appointment confirmation event via RabbitMQ
        // 5. Return response
        redisCacheService.delete("doctor_availability::" + request.getDoctorId() + "-" + request.getDate());
        notificationPublisher.publishAppointmentConfirmation(userId, appointment);
        return appointmentMapper.toResponse(appointment);
    }
}