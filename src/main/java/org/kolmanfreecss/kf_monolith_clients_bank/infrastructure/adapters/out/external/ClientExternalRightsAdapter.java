package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.adapters.out.external;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports.ClientExternalRightsPort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Default implementation of ExternalRightsService. This is a placeholder
 * implementation that could be replaced with a real external service
 * implementation that connects to an actual rights management system.
 */
@Service
public class ClientExternalRightsAdapter implements ClientExternalRightsPort {

    @Override
    public Set<String> fetchClientRights(UUID clientId) {
        // This is a placeholder implementation
        // In a real scenario, this would make an HTTP call or use another
        // mechanism
        // to fetch rights from an external system
        Set<String> defaultRights = new HashSet<>();
        defaultRights.add("VIEW_ACCOUNT");
        defaultRights.add("MAKE_TRANSFER");
        defaultRights.add("VIEW_STATEMENTS");

        return defaultRights;
    }
}
