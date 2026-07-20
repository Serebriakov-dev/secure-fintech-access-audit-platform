# Domain Glossary

## Identity

A human or service identity authenticated by Keycloak.

The platform stores stable identity references but does not store passwords or implement authentication itself.

## Platform Role

A role that grants access to platform capabilities.

Initial platform roles include:

- `EMPLOYEE`;
- `MANAGER`;
- `APPLICATION_OWNER`;
- `SECURITY_OFFICER`;
- `AUDITOR`;
- `PLATFORM_ADMIN`;
- `SYSTEM_SERVICE`.

Platform roles are managed through Keycloak and enforced through Spring Security.

## Target Application

A protected business system for which the platform manages access.

Initial fictional applications are:

- `PAYMENTS_CORE`;
- `CUSTOMER_DATA`;
- `TREASURY`.

## Business Entitlement

A grantable permission within a target application.

Examples:

- `PAYMENT_VIEWER`;
- `PAYMENT_INITIATOR`;
- `PAYMENT_REVIEWER`;
- `CUSTOMER_DATA_EXPORT`;
- `TREASURY_ADMIN`.

Business entitlements are application domain data. They are not Keycloak platform roles.

## Requester

The identity that creates an access request.

The requester may request access for themselves or, when authorized, for another user.

## Target User

The identity that will receive the requested business entitlement.

The requester and target user may be different identities.

## Access Request

A business request to grant a target user a specific entitlement for a defined duration.

An access request controls submission, policy evaluation, approvals, rejection, cancellation, and provisioning.

## Approval Step

One required decision within an access request workflow.

An approval step defines:

- the required approver type;
- the assigned approver;
- the decision;
- the decision timestamp;
- the decision reason.

## Policy Decision

A structured result produced by the policy-service.

Possible decisions are:

- `ALLOW`;
- `DENY`;
- `REQUIRES_APPROVAL`.

A policy decision also contains the risk level, policy version, reason codes, matched rules, duration restrictions, and required approvals.

## Risk Level

A fixed classification describing the security sensitivity of an entitlement or request.

Initial risk levels are:

- `LOW`;
- `MEDIUM`;
- `HIGH`;
- `CRITICAL`.

## Separation of Duties

A security principle that prevents one identity from holding conflicting entitlements.

For example, the same user must not hold both:

- `PAYMENT_INITIATOR`;
- `PAYMENT_REVIEWER`.

## Access Assignment

A provisioned relationship between a target user and a business entitlement.

An assignment has its own lifecycle independently of the access request:

- `PENDING_PROVISIONING`;
- `ACTIVE`;
- `REVOKED`;
- `EXPIRED`;
- `FAILED`.

## Provisioning

The process of applying an approved entitlement to a target application.

The project initially uses a simulated provisioning adapter with idempotent grant and revoke operations.

## Revocation

The explicit removal of an active assignment before its planned expiration time.

Revocation may be initiated by an authorized user, user deactivation, policy change, or security response.

## Expiration

The automatic end of a time-bound assignment.

An expired assignment cannot become active again. A new access request is required.

## Transactional Outbox

A reliability pattern in which a business change and its outgoing event are stored in the same database transaction.

A separate publisher later sends the stored event to Kafka.

## Audit Event

A versioned record describing a security-relevant action or state transition.

An audit event contains structured and allow-listed data. It must not contain passwords, tokens, session identifiers, or unnecessary personal information.

## Append-Only Audit

An audit storage model in which application operations can insert and read records but cannot update or delete them.

## Tamper-Evident Audit

An audit model that uses cryptographic hash relationships to make unauthorized changes detectable.

Tamper-evident does not mean absolutely immutable against every privileged infrastructure administrator.

## Correlation ID

An identifier used to connect operations belonging to the same business interaction across services and events.

## Trace ID

An observability identifier used to follow technical execution across HTTP, gRPC, and Kafka boundaries.

A trace ID and a business correlation ID may be related but serve different purposes.