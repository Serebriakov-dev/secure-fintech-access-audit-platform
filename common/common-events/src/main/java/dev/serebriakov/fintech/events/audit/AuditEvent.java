package dev.serebriakov.fintech.events.audit;


import java.time.Instant;
import java.util.UUID;

public record AuditEvent(
        UUID eventId,
        int schemaVersion,
        Instant occurredAt,
        String correlationId,
        String sourceService,
        String actorId,
        String action,
        String resourceType,
        String resourceId,
        String outcome
) {
}