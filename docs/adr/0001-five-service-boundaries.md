# ADR-0001: Use Five Explicit Service Boundaries

- Status: Accepted
- Date: 2026-07-20

## Context

The platform must demonstrate access governance, policy evaluation, audit processing, client-oriented aggregation, and cloud-native traffic management.

These responsibilities have different security boundaries, data ownership, communication patterns, scaling characteristics, and failure modes.

A single deployable application would simplify initial development but would provide less opportunity to demonstrate distributed-system contracts, independent security enforcement, reliable event delivery, and service-specific operational behaviour.

Excessive service decomposition would create infrastructure without meaningful domain boundaries.

## Decision

The target platform will contain five independently deployable services:

1. `gateway-service`;
2. `bff-service`;
3. `access-service`;
4. `policy-service`;
5. `audit-service`.

The services will be implemented incrementally. A service must provide a complete architectural responsibility and must not exist only as an empty deployment unit.

## Rationale

### gateway-service

Owns edge routing, traffic controls, rate limiting, security headers, and request correlation.

### bff-service

Owns browser-facing authentication flow, session handling, REST command facade, GraphQL aggregation, and client-specific authorization.

### access-service

Owns entitlement catalog data, access requests, approvals, assignments, provisioning workflow, and transactional outbox.

### policy-service

Owns Separation of Duties, risk classification, duration restrictions, and approval-plan decisions.

### audit-service

Owns Kafka event consumption, deduplication, append-only audit persistence, audit search, and tamper-evidence verification.

This decomposition aligns services with distinct responsibilities rather than technical layers.

## Consequences

### Positive

- service responsibilities and data ownership are explicit;
- policy evaluation remains independent of workflow orchestration;
- audit processing can fail and recover independently;
- edge routing and client aggregation remain separate concerns;
- REST, GraphQL, gRPC, and Kafka can be demonstrated in justified roles;
- service-specific security and observability can be verified.

### Negative

- local development requires additional infrastructure;
- distributed tracing and correlation become necessary;
- service contracts require explicit versioning;
- network failures and partial availability must be handled;
- integration and deployment testing become more complex;
- the implementation requires more time than a modular monolith.

## Alternatives Considered

### Modular Monolith

A modular monolith would reduce operational complexity and remain a valid production architecture for the business domain.

It was not selected as the final target because the project intentionally includes distributed communication, independent audit processing, service identity, and cloud-native deployment.

The implementation will still preserve modular boundaries inside each service.

### More Fine-Grained Microservices

Separate services for users, entitlements, requests, approvals, assignments, and provisioning were considered.

This option was rejected because those capabilities participate in closely related transactional workflows and would create unnecessary distributed consistency problems.

## Verification

Compliance with this decision will be verified through:

- explicit service modules in the monorepo;
- documented data ownership;
- independent service configuration;
- no direct cross-service database access;
- contract tests for service communication;
- deployment and observability evidence for every service.