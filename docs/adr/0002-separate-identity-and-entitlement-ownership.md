# ADR-0002: Separate Identity and Entitlement Ownership

- Status: Accepted
- Date: 2026-07-20

## Context

The platform requires both authentication roles and business application permissions.

Keycloak can manage identities, sessions, realm roles, and authorization policies. The platform also contains a dedicated policy-service and an access-governance domain.

Storing the same business permissions in Keycloak and application databases would create two sources of truth and unclear ownership.

## Decision

Keycloak will own:

- user credentials;
- authentication sessions;
- OAuth2/OIDC flows;
- service identities;
- platform roles.

Initial platform roles include:

- `EMPLOYEE`;
- `MANAGER`;
- `APPLICATION_OWNER`;
- `SECURITY_OFFICER`;
- `AUDITOR`;
- `PLATFORM_ADMIN`;
- `SYSTEM_SERVICE`.

The application domain will own:

- target applications;
- business entitlements;
- access requests;
- approval steps;
- access assignments;
- Separation of Duties rules;
- risk-based policy decisions.

Application records will reference identities through stable Keycloak subject identifiers.

The platform will not store user passwords.

## Rationale

Platform roles describe what a user may do inside the access-governance platform.

Business entitlements describe what access a user may receive in an external business application.

Keeping these concepts separate prevents identity configuration from becoming the source of business workflow state.

## Consequences

### Positive

- identity and business authorization have explicit ownership;
- password storage remains outside application services;
- access assignments can have independent approval, duration, and audit history;
- policy-service remains responsible for domain security decisions;
- Keycloak can be replaced through standards-based OAuth2/OIDC integration.

### Negative

- platform roles and business entitlements require separate administration;
- identity references must be synchronized carefully;
- services must validate both security claims and application-domain state;
- user deactivation must trigger application-side revocation workflows.

## Alternatives Considered

### Store All Roles in Keycloak

This would simplify basic role checks but would make time-bound assignments, approval history, entitlement metadata, and Separation of Duties harder to model consistently.

### Store Credentials in access-service

This was rejected because authentication is not part of the access-service business responsibility.

## Verification

Compliance will be verified through:

- absence of password fields in application databases;
- Keycloak realm configuration for platform roles;
- application-owned entitlement and assignment tables;
- object-level authorization tests;
- user-deactivation and revocation tests.