package com.healthcaremanagement.infrastructure.messaging.rabbitmq.publisher;

import com.healthcaremanagement.testresult.entity.TestParameter;
import com.healthcaremanagement.testresult.entity.TestReport;
import com.healthcaremanagement.testresult.event.AbnormalResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbnormalResultPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.abnormal}")
    private String abnormalExchange;

    @Value("${rabbitmq.routing.abnormal}")
    private String abnormalRoutingKey;

    public void publishAbnormal(TestReport report) {
        List<AbnormalResultEvent.AbnormalParameter> abnormalParams = report.getParameters().stream()
                .filter(p -> !"Normal".equalsIgnoreCase(p.getStatus()))
                .map(p -> new AbnormalResultEvent.AbnormalParameter(p.getName(), p.getValue(), p.getStatus()))
                .collect(Collectors.toList());

        if (abnormalParams.isEmpty()) return;

        AbnormalResultEvent event = new AbnormalResultEvent(
                report.getPatientId(),
                report.getReportTitle(),
                report.getReportDate(),
                abnormalParams
        );
        rabbitTemplate.convertAndSend(abnormalExchange, abnormalRoutingKey, event);
        log.info("Published abnormal result event for patient {}: {}", report.getPatientId(), report.getReportTitle());
    }
}