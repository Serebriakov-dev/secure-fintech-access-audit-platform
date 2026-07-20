# Architecture Decision Records

Architecture Decision Records document significant architectural decisions, their context, rationale, and consequences.

## Statuses

- `Proposed` — the decision is under consideration.
- `Accepted` — the decision is approved for implementation.
- `Superseded` — a later ADR has replaced the decision.
- `Deprecated` — the decision is no longer recommended but has not yet been replaced.

## Naming

ADR files use the format `NNNN-short-decision-title.md`.

Example: `0001-five-service-boundaries.md`.

ADR numbers are never reused.

## Decision Index

| ADR | Decision | Status |
|---|---|---|
| [0001](0001-five-service-boundaries.md) | Use five explicit service boundaries | Accepted |
| [0002](0002-separate-identity-and-entitlement-ownership.md) | Separate identity and entitlement ownership | Accepted |
| [0003](0003-allocate-integration-protocols-by-responsibility.md) | Allocate integration protocols by responsibility | Accepted |

## Change Policy

An accepted ADR is not rewritten to hide an earlier decision.

A materially changed decision requires a new ADR that supersedes the previous record.