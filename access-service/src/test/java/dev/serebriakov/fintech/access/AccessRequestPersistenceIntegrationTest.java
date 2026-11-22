package dev.serebriakov.fintech.access;

import dev.serebriakov.fintech.access.application.port.out.AccessRequestStore;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import dev.serebriakov.fintech.access.domain.AccessRequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AccessRequestPersistenceIntegrationTest {

    @Autowired
    private AccessRequestStore accessRequestStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void storesPendingAccessRequest() {
        AccessRequest request = new AccessRequest(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                Instant.parse("2026-11-22T10:00:00Z"),
                "correlation-100",
                "user-100",
                "VIEW_ACCOUNT",
                "ACCOUNT",
                "account-100",
                AccessRequestStatus.PENDING
        );

        accessRequestStore.save(request);

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM access_requests WHERE request_id = ?",
                Long.class,
                request.requestId()
        );
        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM access_requests WHERE request_id = ?",
                String.class,
                request.requestId()
        );

        assertThat(count).isEqualTo(1L);
        assertThat(status).isEqualTo("PENDING");
    }
}
