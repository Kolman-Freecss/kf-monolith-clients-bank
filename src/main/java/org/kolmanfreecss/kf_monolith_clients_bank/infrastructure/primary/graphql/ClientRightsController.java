package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services.ClientCenterRightsService;
import org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types.ClientRightsType;
import org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types.PagedClientRightsType;
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

    @QueryMapping
    public PagedClientRightsType getClientRightsPaginated(
            @Argument String clientId,
            @Argument(name = "pagination") Map<String, Integer> pagination) {

        Set<String> allRights = clientCenterRightsService.getClientRights(UUID.fromString(clientId));
        List<String> rightsList = new ArrayList<>(allRights);

        // Sort the rights alphabetically
        rightsList.sort(String::compareTo);

        // Calculate pagination
        int page = pagination.getOrDefault("page", 0);
        int size = pagination.getOrDefault("size", 10);
        int totalElements = rightsList.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Calculate start and end indices
        int start = page * size;
        int end = Math.min(start + size, totalElements);

        // Get the paginated subset
        List<String> paginatedRights = rightsList.subList(start, end);

        return new PagedClientRightsType(
                paginatedRights,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0);
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
