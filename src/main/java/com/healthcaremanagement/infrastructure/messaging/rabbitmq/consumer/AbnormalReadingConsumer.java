package com.healthcaremanagement.infrastructure.messaging.rabbitmq.consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class AbnormalReadingConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.abnormal}")
    public void handleAbnormalReading(AbnormalReadingEvent event) {
        log.info("Abnormal reading for patient {}: {}", event.getPatientId(), event.getReadingValue());
        // fetch patient contact info and send alert
        notificationService.sendAlert(event.getPatientId(), "Abnormal " + event.getConditionType() + " reading: " + event.getReadingValue());
    }
}
