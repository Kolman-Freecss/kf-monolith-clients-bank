package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql;

import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services.ClientCenterRightsService;
import org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types.ClientRightsType;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClientRightsController {

    private final ClientCenterRightsService clientCenterRightsService;

    @QueryMapping
    public ClientRightsType getClientRights(@Argument String clientId) {
        Set<String> rights = clientCenterRightsService.getClientRights(UUID.fromString(clientId));
        return new ClientRightsType(clientId, rights);
    }

    @MutationMapping
    public ClientRightsType updateClientRights(@Argument String clientId, @Argument Set<String> rights) {
        Set<String> updatedRights = clientCenterRightsService.updateClientRights(UUID.fromString(clientId), rights);
        return new ClientRightsType(clientId, updatedRights);
    }

    @MutationMapping
    public Boolean invalidateClientRightsCache(@Argument String clientId) {
        clientCenterRightsService.invalidateClientRightsCache(UUID.fromString(clientId));
        return true;
    }

    @MutationMapping
    public Boolean invalidateAllClientRightsCache() {
        clientCenterRightsService.invalidateAllClientRightsCache();
        return true;
    }
}
