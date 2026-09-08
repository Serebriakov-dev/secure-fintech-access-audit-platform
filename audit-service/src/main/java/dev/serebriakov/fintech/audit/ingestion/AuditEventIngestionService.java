package dev.serebriakov.fintech.audit.ingestion;

import dev.serebriakov.fintech.audit.application.port.out.AuditEventStore;
import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditEventIngestionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AuditEventIngestionService.class);

    private final AuditEventStore eventStore;

    public AuditEventIngestionService(AuditEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Transactional
    public void ingest(AuditEvent event) {
        boolean stored = eventStore.append(event);

        if (stored) {
            LOGGER.info(
                    "Audit event stored: eventId={}, sourceService={}, action={}, "
                            + "resourceType={}, resourceId={}, outcome={}",
                    event.eventId(),
                    event.sourceService(),
                    event.action(),
                    event.resourceType(),
                    event.resourceId(),
                    event.outcome()
            );
        } else {
            LOGGER.info(
                    "Duplicate audit event ignored: eventId={}",
                    event.eventId()
            );
        }
    }
}