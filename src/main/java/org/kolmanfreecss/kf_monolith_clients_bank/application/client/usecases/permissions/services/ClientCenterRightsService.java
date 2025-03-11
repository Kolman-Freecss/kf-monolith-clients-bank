package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services;

import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports.ClientExternalRightsPort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing client rights and permissions with caching
 * support.
 * Provides operations for retrieving, updating, and invalidating client rights
 * while maintaining a cache to improve performance.
 *
 * Features:
 * - Caching of client rights
 * - Transactional operations
 * - Integration with external rights service
 * - Cache invalidation capabilities
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ClientCenterRightsService {

    private final ClientExternalRightsPort externalRightsService;

    /**
     * Retrieves client rights from cache or external service.
     * Results are cached unless null or empty.
     *
     * @param clientId The UUID of the client
     * @return Set of rights associated with the client
     */
    @Cacheable(value = "clientRights", key = "#clientId", unless = "#result == null || #result.isEmpty()")
    @Transactional(readOnly = true)
    public Set<String> getClientRights(UUID clientId) {
        return externalRightsService.fetchClientRights(clientId);
    }

    /**
     * Updates client rights and refreshes cache.
     *
     * @param clientId  The UUID of the client
     * @param newRights The new set of rights to assign
     * @return Updated set of rights
     */
    @CachePut(value = "clientRights", key = "#clientId")
    @Transactional
    public Set<String> updateClientRights(UUID clientId, Set<String> newRights) {
        // Here we will simulate the update in the external service
        // In a real case, you would call the corresponding port method
        externalRightsService.updateClientRights(clientId, newRights);
        return newRights; // The returned value will be stored in cache
    }

    /**
     * Invalidates cache for a specific client.
     *
     * @param clientId The UUID of the client whose cache should be invalidated
     */
    @CacheEvict(value = "clientRights", key = "#clientId")
    public void invalidateClientRightsCache(UUID clientId) {
        // This method only invalidates the cache for the specified client
    }

    /**
     * Invalidates cache for all clients.
     */
    @CacheEvict(value = "clientRights", allEntries = true)
    public void invalidateAllClientRightsCache() {
        // This method invalidates all client rights cache entries
    }
}
