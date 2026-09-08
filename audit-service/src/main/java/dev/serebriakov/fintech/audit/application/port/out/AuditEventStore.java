package dev.serebriakov.fintech.audit.application.port.out;

import dev.serebriakov.fintech.events.audit.AuditEvent;

public interface AuditEventStore {

    boolean append(AuditEvent event);
}