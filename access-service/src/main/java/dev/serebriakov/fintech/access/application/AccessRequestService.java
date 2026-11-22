package dev.serebriakov.fintech.access.application;

import dev.serebriakov.fintech.access.application.port.out.AccessRequestStore;
import dev.serebriakov.fintech.access.audit.AuditEventFactory;
import dev.serebriakov.fintech.access.audit.KafkaAuditEventPublisher;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import dev.serebriakov.fintech.access.domain.AccessRequestStatus;
import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AccessRequestService {

    private static final String AUDIT_ACTION = "ACCESS_REQUEST_CREATED";
    private static final String AUDIT_RESOURCE_TYPE = "ACCESS_REQUEST";
    private static final String AUDIT_OUTCOME = "SUCCESS";

    private final AccessRequestStore accessRequestStore;
    private final AuditEventFactory auditEventFactory;
    private final KafkaAuditEventPublisher auditEventPublisher;

    public AccessRequestService(
            AccessRequestStore accessRequestStore,
            AuditEventFactory auditEventFactory,
            KafkaAuditEventPublisher auditEventPublisher
    ) {
        this.accessRequestStore = accessRequestStore;
        this.auditEventFactory = auditEventFactory;
        this.auditEventPublisher = auditEventPublisher;
    }

    @Transactional
    public AccessRequest submit(SubmitAccessRequestCommand command) {
        AccessRequest request = new AccessRequest(
                UUID.randomUUID(),
                Instant.now(),
                command.correlationId(),
                command.requesterId(),
                command.requestedAction(),
                command.resourceType(),
                command.resourceId(),
                AccessRequestStatus.PENDING
        );

        accessRequestStore.save(request);

        AuditEvent auditEvent = auditEventFactory.create(
                request.correlationId(),
                request.requesterId(),
                AUDIT_ACTION,
                AUDIT_RESOURCE_TYPE,
                request.requestId().toString(),
                AUDIT_OUTCOME
        );
        auditEventPublisher.publish(auditEvent);

        return request;
    }
}
