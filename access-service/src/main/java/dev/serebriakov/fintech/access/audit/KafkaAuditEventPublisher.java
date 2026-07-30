package dev.serebriakov.fintech.access.audit;


import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public final class KafkaAuditEventPublisher {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    private final String auditEventsTopic;

    public KafkaAuditEventPublisher(
            KafkaTemplate<String, AuditEvent> kafkaTemplate,
            @Value("${app.kafka.topics.audit-events}") String auditEventsTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.auditEventsTopic = auditEventsTopic;
    }

    public CompletableFuture<SendResult<String, AuditEvent>> publish(
            AuditEvent event
    ) {
        String messageKey =
                event.resourceType() + ":" + event.resourceId();

        return kafkaTemplate.send(
                auditEventsTopic,
                messageKey,
                event
        );
    }
}