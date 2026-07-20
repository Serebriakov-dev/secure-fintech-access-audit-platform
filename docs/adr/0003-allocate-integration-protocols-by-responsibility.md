# ADR-0003: Allocate Integration Protocols by Responsibility

- Status: Accepted
- Date: 2026-07-20

## Context

The target architecture includes REST, GraphQL, gRPC, and Kafka.

Using several protocols without explicit boundaries would increase complexity without architectural value.

Each protocol must solve a distinct communication problem.

## Decision

The platform will allocate protocols as follows:

- REST for commands and conventional service APIs;
- GraphQL for BFF queries, aggregated read models, and authorized subscriptions;
- gRPC for synchronous access-service to policy-service decisions;
- Kafka for durable asynchronous audit and security event delivery.

A protocol must not be introduced outside its defined role without a new architectural decision.

## Rationale

### REST

REST provides clear command semantics, HTTP status codes, idempotency support, validation, and OpenAPI documentation.

### GraphQL

GraphQL allows the BFF to combine access and audit information into client-specific read models without exposing internal service topology.

### gRPC

gRPC provides a strongly typed internal contract for policy decisions, explicit deadlines, structured status handling, and efficient service communication.

### Kafka

Kafka provides durable asynchronous delivery, consumer independence, retry handling, and event replay.

## Consequences

### Positive

- every protocol has a clear purpose;
- service contracts remain understandable;
- command and query responsibilities are separated;
- synchronous and asynchronous failures can be handled explicitly;
- the portfolio demonstrates several integration styles without arbitrary usage.

### Negative

- developers must understand four communication models;
- testing requires multiple contract strategies;
- observability must propagate context across different protocols;
- local infrastructure and debugging become more complex.

## Alternatives Considered

### REST for All Communication

REST-only communication would simplify development but would not provide durable event delivery, GraphQL aggregation, or a strongly typed gRPC policy contract.

### Kafka for Policy Decisions

Asynchronous policy evaluation would complicate immediate request handling and user feedback. Kafka remains appropriate for audit delivery rather than the primary synchronous policy decision.

### GraphQL Directly in Domain Services

This would couple client query requirements to internal business services. GraphQL therefore remains in the BFF layer.

## Verification

Compliance will be verified through:

- OpenAPI contracts for REST interfaces;
- GraphQL schema and resolver tests in bff-service;
- protobuf compatibility tests;
- Kafka event schema and consumer tests;
- architecture documentation and service dependency checks.