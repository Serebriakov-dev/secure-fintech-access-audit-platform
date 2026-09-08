package dev.serebriakov.fintech.audit;

import dev.serebriakov.fintech.audit.ingestion.AuditEventIngestionService;
import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
@Import(TestcontainersConfiguration.class)
class AuditPersistenceIntegrationTest {

    @Autowired
    private AuditEventIngestionService ingestionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void storesEventOnceWhenItIsDeliveredMoreThanOnce() {
        AuditEvent event = event(
                UUID.fromString("11111111-1111-1111-1111-111111111111")
        );

        ingestionService.ingest(event);
        ingestionService.ingest(event);

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM audit_events WHERE event_id = ?",
                Long.class,
                event.eventId()
        );

        assertThat(count).isEqualTo(1L);
    }

    @Test
    void preventsModificationAndDeletionOfStoredEvents() {
        AuditEvent event = event(
                UUID.fromString("22222222-2222-2222-2222-222222222222")
        );

        ingestionService.ingest(event);

        assertThatThrownBy(() -> jdbcTemplate.update(
                "UPDATE audit_events SET outcome = ? WHERE event_id = ?",
                "DENY",
                event.eventId()
        ))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("audit_events is append-only");

        assertThatThrownBy(() -> jdbcTemplate.update(
                "DELETE FROM audit_events WHERE event_id = ?",
                event.eventId()
        ))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("audit_events is append-only");
    }

    private AuditEvent event(UUID eventId) {
        return new AuditEvent(
                eventId,
                1,
                Instant.parse("2026-07-30T12:00:00Z"),
                "correlation-100",
                "access-service",
                "user-100",
                "VIEW_ACCOUNT",
                "ACCOUNT",
                "account-100",
                "ALLOW"
        );
    }
}