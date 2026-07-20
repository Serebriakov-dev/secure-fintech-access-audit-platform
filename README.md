# Secure FinTech Access & Audit Platform

Cloud-native access governance and tamper-evident audit platform for regulated financial environments.

> **Project status:** Architecture and repository foundation. Active development has not started yet.

## Overview

The platform manages business entitlement requests, evaluates security policies, coordinates risk-based approvals, creates time-bound access assignments, and records security-relevant actions in a tamper-evident audit trail.

The project is designed as an engineering portfolio project focused on modern Java, application security, distributed systems, cloud-native deployment, observability, and DevSecOps.

## Planned Core Capabilities

- OAuth2/OIDC authentication with Keycloak
- platform roles and object-level authorization
- business entitlement catalog
- access request and approval workflows
- Separation of Duties policy evaluation
- risk-based and multi-stage approvals
- time-bound assignments, revocation, and expiration
- gRPC-based policy decisions
- transactional outbox
- Kafka-based audit event delivery
- idempotent event processing
- tamper-evident audit storage
- REST command APIs
- GraphQL dashboard and audit queries
- API Gateway and browser-facing BFF
- distributed tracing, metrics, and structured logging
- Docker Compose, Kubernetes, Helm, and Terraform
- security-focused CI/CD and software supply-chain controls

## Planned Services

| Service | Responsibility |
|---|---|
| `gateway-service` | Edge routing, traffic controls, security headers, rate limiting, and correlation context |
| `bff-service` | Browser-facing REST commands, GraphQL queries, secure sessions, and response aggregation |
| `access-service` | Entitlement catalog, access requests, approvals, assignments, and provisioning workflow |
| `policy-service` | Separation of Duties, risk evaluation, duration limits, and approval requirements |
| `audit-service` | Kafka event processing, audit search, deduplication, and tamper-evidence verification |

## Technology Baseline

- Java 25 LTS
- Spring Boot 4.1
- Spring Cloud Gateway
- Spring Security
- Spring for GraphQL
- Spring gRPC
- Spring for Apache Kafka
- PostgreSQL and Liquibase
- Keycloak
- Redis
- Docker Compose
- Kubernetes and Helm
- Terraform and AWS EKS
- OpenTelemetry, Prometheus, and Grafana
- Maven and GitHub Actions

## Architecture

The current architecture foundation includes:

- [System Context](docs/architecture/system-context.md)
- [Container View](docs/architecture/container-view.md)
- [Domain Glossary](docs/architecture/domain-glossary.md)
- [Quality Attributes](docs/architecture/quality-attributes.md)
- [Architecture Decision Records](docs/adr/README.md)

The architecture will evolve through explicit Architecture Decision Records as implementation progresses.
## Development Approach

The platform is implemented incrementally through independently reviewable Pull Requests. Each milestone must leave the repository in a working and demonstrable state.

All source code, documentation, commits, Pull Requests, and technical discussions stored in the repository are written in English.

## Current Milestone

**Milestone 0 — Architecture and repository foundation**

Current objectives:

- repository governance
- reproducible build foundation
- service boundaries
- domain glossary
- architecture decision records
- initial threat model
- continuous integration baseline

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.