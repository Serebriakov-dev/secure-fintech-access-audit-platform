package dev.serebriakov.fintech.access.application.port.out;

import dev.serebriakov.fintech.access.domain.AccessRequest;

public interface AccessRequestStore {

    void save(AccessRequest request);
}
