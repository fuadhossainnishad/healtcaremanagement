package com.healthcaremanagement.medicine.service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final OrderRepository orderRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final RedisCacheService redisCacheService;
    private final RabbitTemplate rabbitTemplate; // for order notifications

    @Override
    @Cacheable(value = "medicines", key = "#query + '-' + #pageable.pageNumber")
    public Page<MedicineResponse> searchMedicines(String query, Pageable pageable) {
        return medicineRepository.findByNameContainingIgnoreCaseOrGenericNameContainingIgnoreCase(query, query, pageable)
                .map(medicineMapper::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String userId, OrderRequest request) {
        // Validate cart items, calculate total, create order with status PENDING_PRESCRIPTION
        // Publish event to RabbitMQ for notification (async)
        Order order = Order.builder()
                .patientId(userId)
                .totalAmount(calculateTotal(request))
                .status(OrderStatus.PENDING_PRESCRIPTION)
                .build();
        order = orderRepository.save(order);
        // ...
        rabbitTemplate.convertAndSend("order.exchange", "order.created", new OrderCreatedEvent(order));
        return orderMapper.toResponse(order);
    }
}
