package dev.serebriakov.fintech.events.audit;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditEventTest {

    @Test
    void shouldPreserveAllContractFields() {
        UUID eventId = UUID.fromString(
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );
        Instant occurredAt = Instant.parse("2026-07-28T09:00:00Z");

        AuditEvent event = new AuditEvent(
                eventId,
                1,
                occurredAt,
                "correlation-123",
                "access-service",
                "user-42",
                "READ_ACCOUNT",
                "ACCOUNT",
                "account-100",
                "SUCCESS"
        );

        assertAll(
                () -> assertEquals(eventId, event.eventId()),
                () -> assertEquals(1, event.schemaVersion()),
                () -> assertEquals(occurredAt, event.occurredAt()),
                () -> assertEquals("correlation-123", event.correlationId()),
                () -> assertEquals("access-service", event.sourceService()),
                () -> assertEquals("user-42", event.actorId()),
                () -> assertEquals("READ_ACCOUNT", event.action()),
                () -> assertEquals("ACCOUNT", event.resourceType()),
                () -> assertEquals("account-100", event.resourceId()),
                () -> assertEquals("SUCCESS", event.outcome())
        );
    }
}