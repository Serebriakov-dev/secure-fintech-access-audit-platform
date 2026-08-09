package dev.serebriakov.fintech.audit.ingestion;

import dev.serebriakov.fintech.events.audit.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditEventIngestionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AuditEventIngestionService.class);

    public void ingest(AuditEvent event) {
        LOGGER.info(
                "Audit event received: eventId={}, sourceService={}, action={}, "
                        + "resourceType={}, resourceId={}, outcome={}",
                event.eventId(),
                event.sourceService(),
                event.action(),
                event.resourceType(),
                event.resourceId(),
                event.outcome()
        );
    }
}