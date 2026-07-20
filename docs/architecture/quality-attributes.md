# Quality Attributes

## Purpose

This document defines the principal quality attributes and measurable design goals of the Secure FinTech Access & Audit Platform.

These goals guide architectural decisions and will be verified incrementally as the platform is implemented.

## Priority Order

When design goals conflict, the platform uses the following priority order:

1. security and authorization correctness;
2. business and audit consistency;
3. recoverability and operational visibility;
4. maintainability and testability;
5. performance and infrastructure efficiency.

The platform must never grant access merely to preserve availability.

## Security

### Goals

- every request is authenticated unless explicitly documented as public;
- authorization is deny-by-default;
- downstream services enforce their own authorization boundaries;
- platform roles and business entitlements remain separate concepts;
- object-level authorization protects requests, approvals, assignments, and audit evidence;
- service identities use narrowly scoped permissions;
- credentials, tokens, cookies, and secrets never appear in logs or audit events;
- security-sensitive operations produce audit evidence.

### Verification

- authorization matrix tests;
- negative-path integration tests;
- object-level authorization tests;
- token issuer and audience validation tests;
- log and event payload inspection;
- threat-model traceability.

## Reliability

### Goals

- a committed business transition eventually produces its corresponding audit event;
- temporary Kafka unavailability creates a recoverable outbox backlog;
- duplicate event delivery does not create duplicate audit records;
- policy-service unavailability never results in an access grant;
- retries do not repeat completed business effects;
- scheduled expiration and revocation operations are idempotent.

### Verification

- transactional outbox integration tests;
- duplicate-delivery tests;
- service-outage tests;
- retry and restart tests;
- unique database constraints;
- operational backlog metrics.

## Business Consistency

### Goals

- invalid state transitions are rejected;
- concurrent approval attempts produce one valid transition;
- high-risk access is re-evaluated before provisioning;
- expired or revoked assignments cannot become active again;
- a new request is required for a new assignment;
- database constraints support critical domain invariants.

### Verification

- aggregate unit tests;
- optimistic-locking tests;
- concurrent-request tests;
- persistence integration tests;
- property-based policy tests.

## Audit Integrity

### Goals

- application credentials cannot update or delete audit records;
- audit events use versioned and typed payloads;
- every event has a stable event identifier;
- duplicate events are detected;
- unauthorized record modification is detectable through hash-chain verification;
- administrative replay and recovery operations are themselves audited.

### Verification

- database privilege tests;
- event-schema contract tests;
- duplicate-consumer tests;
- audit-chain verification tests;
- deliberate tampering tests.

The audit model is described as tamper-evident and append-only, not absolutely immutable.

## Performance

Initial performance goals apply to a documented local test environment.

### Goals

- normal command API latency: p95 below 300 ms, excluding deliberate external failure simulation;
- policy decision latency: p95 below 100 ms for the defined local scenario;
- committed audit event visible in audit search within 5 seconds under normal conditions;
- collection APIs use bounded pagination;
- GraphQL queries use depth, complexity, and page-size limits;
- reactive streams support cancellation and bounded resource usage.

Performance targets will be adjusted only through documented measurements and architectural decisions.

## Observability

### Goals

- every external request receives a correlation identifier;
- trace context propagates across HTTP, gRPC, and Kafka boundaries;
- logs use consistent structured fields;
- business and technical failures are distinguishable;
- outbox backlog, Kafka lag, policy failures, and dead-letter activity are measurable;
- liveness and readiness represent different operational conditions.

### Verification

- distributed trace inspection;
- structured-log tests;
- metrics integration tests;
- dashboard and alert demonstrations;
- deployment health-check tests.

## Maintainability

### Goals

- every service has explicit responsibilities and data ownership;
- domain logic remains independent of transport and persistence technology;
- services do not read another service's database;
- shared modules do not contain business entities or domain rules;
- API, protobuf, and event contracts are versioned;
- architecture boundaries are verified automatically where practical;
- repository documentation and implementation remain consistent.

### Verification

- architecture tests;
- module-boundary tests;
- contract compatibility checks;
- focused Pull Request reviews;
- Architecture Decision Records.

## Testability

### Goals

- domain rules can be tested without starting Spring;
- time-dependent behaviour uses an injected clock;
- external integrations are accessed through ports;
- integration tests use production-compatible infrastructure where practical;
- critical authorization and policy rules include negative-path tests;
- failure behaviour is reproducible.

## Privacy

### Goals

- stable subject identifiers are preferred over email addresses in internal events;
- audit payloads use allow-listed fields;
- free-form metadata is not accepted into security events;
- demo identities and data are fictional;
- personal data exposure is considered in threat modelling;
- retention responsibilities are documented without claiming a legal certification.

## Deployability

### Goals

- the local platform starts through documented Docker Compose profiles;
- containers run as non-root users;
- Kubernetes readiness prevents premature traffic;
- Helm deployment is verified in a local kind cluster;
- Terraform uses cost-safe defaults and does not create paid infrastructure automatically;
- environment-specific configuration remains outside application binaries.

## Current Status

The quality attributes in this document are architectural targets.

Implementation evidence will be added incrementally through tests, CI reports, metrics, traces, deployment smoke tests, and release documentation.