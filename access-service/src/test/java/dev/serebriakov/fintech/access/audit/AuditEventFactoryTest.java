package dev.serebriakov.fintech.access.audit;

import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuditEventFactoryTest {

    private final AuditEventFactory factory = new AuditEventFactory();

    @Test
    void shouldCreateAuditEvent() {
        Instant beforeCreation = Instant.now();

        AuditEvent event = factory.create(
                "correlation-123",
                "user-42",
                "READ_ACCOUNT",
                "ACCOUNT",
                "account-100",
                "SUCCESS"
        );

        Instant afterCreation = Instant.now();

        assertAll(
                () -> assertNotNull(event.eventId()),
                () -> assertFalse(event.occurredAt().isBefore(beforeCreation)),
                () -> assertFalse(event.occurredAt().isAfter(afterCreation)),
                () -> assertEquals(1, event.schemaVersion()),
                () -> assertEquals("access-service", event.sourceService()),
                () -> assertEquals("correlation-123", event.correlationId()),
                () -> assertEquals("user-42", event.actorId()),
                () -> assertEquals("READ_ACCOUNT", event.action()),
                () -> assertEquals("ACCOUNT", event.resourceType()),
                () -> assertEquals("account-100", event.resourceId()),
                () -> assertEquals("SUCCESS", event.outcome())
        );
    }
}