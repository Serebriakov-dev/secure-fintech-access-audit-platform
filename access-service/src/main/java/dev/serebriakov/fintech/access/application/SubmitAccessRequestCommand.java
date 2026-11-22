package dev.serebriakov.fintech.access.application;

public record SubmitAccessRequestCommand(
        String correlationId,
        String requesterId,
        String requestedAction,
        String resourceType,
        String resourceId
) {
}
