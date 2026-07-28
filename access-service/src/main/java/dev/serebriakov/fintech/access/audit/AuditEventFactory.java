package dev.serebriakov.fintech.access.audit;

import dev.serebriakov.fintech.events.audit.AuditEvent;

import java.time.Instant;
import java.util.UUID;

public final class AuditEventFactory {

    private static final int SCHEMA_VERSION = 1;
    private static final String SOURCE_SERVICE = "access-service";

    public AuditEvent create(
            String correlationId,
            String actorId,
            String action,
            String resourceType,
            String resourceId,
            String outcome
    ) {
        return new AuditEvent(
                UUID.randomUUID(),
                SCHEMA_VERSION,
                Instant.now(),
                correlationId,
                SOURCE_SERVICE,
                actorId,
                action,
                resourceType,
                resourceId,
                outcome
        );
    }
}