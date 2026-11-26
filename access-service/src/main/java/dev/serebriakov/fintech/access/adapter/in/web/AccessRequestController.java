package dev.serebriakov.fintech.access.adapter.in.web;

import dev.serebriakov.fintech.access.application.AccessRequestService;
import dev.serebriakov.fintech.access.application.SubmitAccessRequestCommand;
import dev.serebriakov.fintech.access.domain.AccessRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessRequestResponse submit(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateAccessRequestRequest request
    ) {
        AccessRequest accessRequest = accessRequestService.submit(
                new SubmitAccessRequestCommand(
                        request.correlationId(),
                        jwt.getSubject(),
                        request.requestedAction(),
                        request.resourceType(),
                        request.resourceId()
                )
        );
        return AccessRequestResponse.from(accessRequest);
    }
}
