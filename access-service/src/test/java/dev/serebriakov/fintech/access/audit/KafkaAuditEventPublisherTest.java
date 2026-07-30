package dev.serebriakov.fintech.access.audit;

import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaAuditEventPublisherTest {

    @Mock
    private KafkaTemplate<String, AuditEvent> kafkaTemplate;

    @Test
    void shouldPublishEventWithResourceKey() {
        KafkaAuditEventPublisher publisher =
                new KafkaAuditEventPublisher(kafkaTemplate, "audit-events");

        AuditEvent event = new AuditEvent(
                UUID.randomUUID(),
                1,
                Instant.now(),
                "correlation-123",
                "access-service",
                "user-42",
                "READ_ACCOUNT",
                "ACCOUNT",
                "account-100",
                "SUCCESS"
        );

        CompletableFuture<SendResult<String, AuditEvent>> future =
                CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(
                "audit-events",
                "ACCOUNT:account-100",
                event
        )).thenReturn(future);

        CompletableFuture<SendResult<String, AuditEvent>> result =
                publisher.publish(event);

        assertSame(future, result);

        verify(kafkaTemplate).send(
                "audit-events",
                "ACCOUNT:account-100",
                event
        );
    }
}