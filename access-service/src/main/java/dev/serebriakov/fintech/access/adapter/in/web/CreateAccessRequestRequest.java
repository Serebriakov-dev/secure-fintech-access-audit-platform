package dev.serebriakov.fintech.access.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccessRequestRequest(
        @NotBlank
        @Size(max = 128)
        String correlationId,

        @NotBlank
        @Size(max = 255)
        String requesterId,

        @NotBlank
        @Size(max = 100)
        String requestedAction,

        @NotBlank
        @Size(max = 100)
        String resourceType,

        @NotBlank
        @Size(max = 255)
        String resourceId
) {
}
