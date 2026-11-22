package dev.serebriakov.fintech.access.adapter.in.web;

import dev.serebriakov.fintech.access.domain.AccessRequest;
import dev.serebriakov.fintech.access.domain.AccessRequestStatus;

import java.time.Instant;
import java.util.UUID;

public record AccessRequestResponse(
        UUID requestId,
        Instant createdAt,
        String correlationId,
        String requesterId,
        String requestedAction,
        String resourceType,
        String resourceId,
        AccessRequestStatus status
) {
    static AccessRequestResponse from(AccessRequest request) {
        return new AccessRequestResponse(
                request.requestId(), request.createdAt(),
                request.correlationId(), request.requesterId(),
                request.requestedAction(), request.resourceType(),
                request.resourceId(), request.status()
        );
    }
}
