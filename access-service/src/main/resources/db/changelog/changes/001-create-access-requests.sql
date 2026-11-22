--liquibase formatted sql

--changeset serebriakov:001-create-access-requests
CREATE TABLE access_requests
(
    request_id       UUID PRIMARY KEY,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    correlation_id   VARCHAR(128)             NOT NULL,
    requester_id     VARCHAR(255)             NOT NULL,
    requested_action VARCHAR(100)             NOT NULL,
    resource_type    VARCHAR(100)             NOT NULL,
    resource_id      VARCHAR(255)             NOT NULL,
    status           VARCHAR(32)              NOT NULL
        CHECK (status IN ('PENDING', 'APPROVED', 'DENIED'))
);

CREATE INDEX idx_access_requests_requester_id
    ON access_requests (requester_id);

CREATE INDEX idx_access_requests_correlation_id
    ON access_requests (correlation_id);

CREATE INDEX idx_access_requests_resource
    ON access_requests (resource_type, resource_id);

--rollback DROP TABLE access_requests;
