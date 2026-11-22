package dev.serebriakov.fintech.access.adapter.out.persistence;

import dev.serebriakov.fintech.access.application.port.out.AccessRequestStore;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Repository
public class JdbcAccessRequestStore implements AccessRequestStore {

    private static final String INSERT_SQL = """
            INSERT INTO access_requests (
                request_id,
                created_at,
                correlation_id,
                requester_id,
                requested_action,
                resource_type,
                resource_id,
                status
            )
            VALUES (
                :requestId,
                :createdAt,
                :correlationId,
                :requesterId,
                :requestedAction,
                :resourceType,
                :resourceId,
                :status
            )
            """;

    private final JdbcClient jdbcClient;

    public JdbcAccessRequestStore(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(AccessRequest request) {
        int updatedRows = jdbcClient.sql(INSERT_SQL)
                .param("requestId", request.requestId())
                .param(
                        "createdAt",
                        OffsetDateTime.ofInstant(
                                request.createdAt(),
                                ZoneOffset.UTC
                        )
                )
                .param("correlationId", request.correlationId())
                .param("requesterId", request.requesterId())
                .param("requestedAction", request.requestedAction())
                .param("resourceType", request.resourceType())
                .param("resourceId", request.resourceId())
                .param("status", request.status().name())
                .update();

        if (updatedRows != 1) {
            throw new IllegalStateException("Access request was not stored");
        }
    }
}
