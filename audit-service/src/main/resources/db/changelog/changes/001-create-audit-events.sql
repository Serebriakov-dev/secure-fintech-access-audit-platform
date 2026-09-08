--liquibase formatted sql

--changeset serebriakov:001-create-audit-events
CREATE TABLE audit_events
(
    event_id       UUID PRIMARY KEY,
    schema_version INTEGER                  NOT NULL
        CHECK (schema_version > 0),
    occurred_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    received_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    correlation_id VARCHAR(128)             NOT NULL,
    source_service VARCHAR(100)             NOT NULL,
    actor_id       VARCHAR(255)             NOT NULL,
    action         VARCHAR(100)             NOT NULL,
    resource_type  VARCHAR(100)             NOT NULL,
    resource_id    VARCHAR(255)             NOT NULL,
    outcome        VARCHAR(50)              NOT NULL
);

CREATE INDEX idx_audit_events_occurred_at
    ON audit_events (occurred_at);

CREATE INDEX idx_audit_events_correlation_id
    ON audit_events (correlation_id);

--rollback DROP TABLE audit_events;

--changeset serebriakov:002-create-audit-mutation-guard splitStatements:false
CREATE FUNCTION reject_audit_event_mutation()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    RAISE EXCEPTION 'audit_events is append-only';
END;
$$;
--rollback DROP FUNCTION reject_audit_event_mutation();

--changeset serebriakov:003-protect-audit-events
CREATE TRIGGER audit_events_no_update_or_delete
    BEFORE UPDATE OR DELETE
ON audit_events
    FOR EACH ROW
EXECUTE FUNCTION reject_audit_event_mutation();

--rollback DROP TRIGGER audit_events_no_update_or_delete ON audit_events;