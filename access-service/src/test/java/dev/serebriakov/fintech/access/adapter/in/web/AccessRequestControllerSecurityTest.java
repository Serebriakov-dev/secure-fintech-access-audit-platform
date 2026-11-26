package dev.serebriakov.fintech.access.adapter.in.web;

import dev.serebriakov.fintech.access.application.AccessRequestService;
import dev.serebriakov.fintech.access.application.SubmitAccessRequestCommand;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import dev.serebriakov.fintech.access.domain.AccessRequestStatus;
import dev.serebriakov.fintech.access.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccessRequestController.class)
@Import(SecurityConfiguration.class)
class AccessRequestControllerSecurityTest {

    private static final String REQUEST_BODY = """
            {
              "correlationId": "correlation-100",
              "requestedAction": "VIEW_ACCOUNT",
              "resourceType": "ACCOUNT",
              "resourceId": "account-100"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccessRequestService accessRequestService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void rejectsUnauthenticatedRequest() throws Exception {
        mockMvc.perform(post("/api/v1/access-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(accessRequestService);
    }

    @Test
    void rejectsAuthenticatedUserWithoutRequesterRole() throws Exception {
        mockMvc.perform(post("/api/v1/access-requests")
                        .with(jwt().jwt(token -> token.subject("user-100"))
                                .authorities(new SimpleGrantedAuthority("ROLE_AUDITOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isForbidden());

        verifyNoInteractions(accessRequestService);
    }

    @Test
    void usesAuthenticatedSubjectAsRequesterId() throws Exception {
        AccessRequest accessRequest = new AccessRequest(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                Instant.parse("2026-11-26T10:00:00Z"),
                "correlation-100",
                "user-100",
                "VIEW_ACCOUNT",
                "ACCOUNT",
                "account-100",
                AccessRequestStatus.PENDING
        );
        when(accessRequestService.submit(any(SubmitAccessRequestCommand.class)))
                .thenReturn(accessRequest);

        mockMvc.perform(post("/api/v1/access-requests")
                        .with(jwt().jwt(token -> token.subject("user-100"))
                                .authorities(new SimpleGrantedAuthority(
                                        "ROLE_ACCESS_REQUESTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requesterId").value("user-100"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        var commandCaptor = org.mockito.ArgumentCaptor
                .forClass(SubmitAccessRequestCommand.class);
        verify(accessRequestService).submit(commandCaptor.capture());
        assertThat(commandCaptor.getValue().requesterId()).isEqualTo("user-100");
    }
}
