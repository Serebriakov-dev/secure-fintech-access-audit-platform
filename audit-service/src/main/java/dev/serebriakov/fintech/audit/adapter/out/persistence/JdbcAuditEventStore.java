package dev.serebriakov.fintech.audit.adapter.out.persistence;

import dev.serebriakov.fintech.audit.application.port.out.AuditEventStore;
import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Repository
public class JdbcAuditEventStore implements AuditEventStore {

    private static final String INSERT_SQL = """
            INSERT INTO audit_events (
                event_id,
                schema_version,
                occurred_at,
                correlation_id,
                source_service,
                actor_id,
                action,
                resource_type,
                resource_id,
                outcome
            )
            VALUES (
                :eventId,
                :schemaVersion,
                :occurredAt,
                :correlationId,
                :sourceService,
                :actorId,
                :action,
                :resourceType,
                :resourceId,
                :outcome
            )
            ON CONFLICT (event_id) DO NOTHING
            """;

    private final JdbcClient jdbcClient;

    public JdbcAuditEventStore(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public boolean append(AuditEvent event) {
        return jdbcClient.sql(INSERT_SQL)
                .param("eventId", event.eventId())
                .param("schemaVersion", event.schemaVersion())
                .param(
                        "occurredAt",
                        OffsetDateTime.ofInstant(
                                event.occurredAt(),
                                ZoneOffset.UTC
                        )
                )
                .param("correlationId", event.correlationId())
                .param("sourceService", event.sourceService())
                .param("actorId", event.actorId())
                .param("action", event.action())
                .param("resourceType", event.resourceType())
                .param("resourceId", event.resourceId())
                .param("outcome", event.outcome())
                .update() == 1;
    }
}