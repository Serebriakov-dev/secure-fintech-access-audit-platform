# Keycloak protection for Access Service

`access-service` is an OAuth 2.0 resource server. It accepts JWT access tokens
issued by Keycloak and does not create HTTP sessions.

## Required Keycloak configuration

- Realm: `secure-fintech`
- Client/audience: `access-service`
- Realm role allowed to create access requests: `ACCESS_REQUESTER`
- The access token must contain the client audience and Keycloak's
  `realm_access.roles` claim.

The service reads the following environment variables:

| Variable | Default |
|---|---|
| `KEYCLOAK_ISSUER_URI` | `http://localhost:8080/realms/secure-fintech` |
| `KEYCLOAK_JWK_SET_URI` | `http://localhost:8080/realms/secure-fintech/protocol/openid-connect/certs` |
| `KEYCLOAK_AUDIENCE` | `access-service` |

Both the issuer and audience are validated. Supplying the JWK Set URI allows
the application to start independently of Keycloak; signing keys are retrieved
when a token must be verified.

## Authorization behavior

- `POST /api/v1/access-requests` requires `ACCESS_REQUESTER`.
- Missing or invalid bearer tokens return HTTP `401`.
- Valid tokens without the required role return HTTP `403`.
- Health endpoints remain publicly available for platform probes.

The requester identity is taken from the JWT `sub` claim. It is no longer
accepted from the request body, preventing a caller from submitting a request
on behalf of another user.
