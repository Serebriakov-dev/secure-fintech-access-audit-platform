package dev.serebriakov.fintech.audit.ingestion;

import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAuditEventConsumer {

    private final AuditEventIngestionService ingestionService;

    public KafkaAuditEventConsumer(AuditEventIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @KafkaListener(topics = "${app.kafka.topics.audit-events}")
    public void consume(AuditEvent event) {
        ingestionService.ingest(event);
    }
}