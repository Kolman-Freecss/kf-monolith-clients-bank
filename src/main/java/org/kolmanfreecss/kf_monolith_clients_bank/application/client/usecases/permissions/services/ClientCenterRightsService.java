package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services;

import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports.ClientExternalRightsPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientCenterRightsService {

    private final ClientExternalRightsPort externalRightsService;

    @Transactional(readOnly = true)
    public Set<String> getClientRights(UUID clientId) {
        return externalRightsService.fetchClientRights(clientId);
    }
}
