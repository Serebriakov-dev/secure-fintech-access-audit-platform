package dev.serebriakov.fintech.audit.ingestion;

import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaAuditEventConsumerTest {

    @Mock
    private AuditEventIngestionService ingestionService;

    @InjectMocks
    private KafkaAuditEventConsumer consumer;

    @Test
    void delegatesReceivedEventToIngestionService() {
        AuditEvent event = new AuditEvent(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                1,
                Instant.parse("2026-07-30T12:00:00Z"),
                "correlation-100",
                "access-service",
                "user-100",
                "VIEW_ACCOUNT",
                "ACCOUNT",
                "account-100",
                "ALLOW"
        );

        consumer.consume(event);

        verify(ingestionService).ingest(event);
    }
}