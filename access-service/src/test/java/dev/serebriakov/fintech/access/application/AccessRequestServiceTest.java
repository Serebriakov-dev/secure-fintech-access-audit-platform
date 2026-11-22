package dev.serebriakov.fintech.access.application;

import dev.serebriakov.fintech.access.application.port.out.AccessRequestStore;
import dev.serebriakov.fintech.access.audit.AuditEventFactory;
import dev.serebriakov.fintech.access.audit.KafkaAuditEventPublisher;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import dev.serebriakov.fintech.access.domain.AccessRequestStatus;
import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessRequestServiceTest {

    @Mock
    private AccessRequestStore accessRequestStore;

    @Mock
    private KafkaAuditEventPublisher auditEventPublisher;

    private AccessRequestService service;

    @BeforeEach
    void setUp() {
        service = new AccessRequestService(
                accessRequestStore,
                new AuditEventFactory(),
                auditEventPublisher
        );
    }

    @Test
    void storesPendingRequestAndPublishesAuditEvent() {
        when(auditEventPublisher.publish(any(AuditEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        SubmitAccessRequestCommand command = new SubmitAccessRequestCommand(
                "correlation-100",
                "user-100",
                "VIEW_ACCOUNT",
                "ACCOUNT",
                "account-100"
        );

        AccessRequest request = service.submit(command);

        assertThat(request.requestId()).isNotNull();
        assertThat(request.createdAt()).isNotNull();
        assertThat(request.status()).isEqualTo(AccessRequestStatus.PENDING);
        assertThat(request.requestedAction()).isEqualTo("VIEW_ACCOUNT");
        verify(accessRequestStore).save(request);

        ArgumentCaptor<AuditEvent> eventCaptor =
                ArgumentCaptor.forClass(AuditEvent.class);
        verify(auditEventPublisher).publish(eventCaptor.capture());

        AuditEvent event = eventCaptor.getValue();
        assertThat(event.correlationId()).isEqualTo("correlation-100");
        assertThat(event.actorId()).isEqualTo("user-100");
        assertThat(event.action()).isEqualTo("ACCESS_REQUEST_CREATED");
        assertThat(event.resourceType()).isEqualTo("ACCESS_REQUEST");
        assertThat(event.resourceId())
                .isEqualTo(request.requestId().toString());
        assertThat(event.outcome()).isEqualTo("SUCCESS");
    }
}
