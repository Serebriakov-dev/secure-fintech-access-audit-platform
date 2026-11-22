package dev.serebriakov.fintech.access.domain;

import java.time.Instant;
import java.util.UUID;

public record AccessRequest(
        UUID requestId,
        Instant createdAt,
        String correlationId,
        String requesterId,
        String requestedAction,
        String resourceType,
        String resourceId,
        AccessRequestStatus status
) {
}
