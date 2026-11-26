package dev.serebriakov.fintech.access.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakRealmRoleConverterTest {

    private final KeycloakRealmRoleConverter converter =
            new KeycloakRealmRoleConverter();

    @Test
    void convertsKeycloakRealmRolesToSpringAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-100")
                .claim("realm_access", Map.of(
                        "roles", List.of("ACCESS_REQUESTER", "AUDITOR")
                ))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ACCESS_REQUESTER", "ROLE_AUDITOR");
    }

    @Test
    void returnsNoAuthoritiesWhenRealmRolesAreMissing() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-100")
                .build();

        assertThat(converter.convert(jwt)).isEmpty();
    }
}
