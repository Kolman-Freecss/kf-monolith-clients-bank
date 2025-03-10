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

@Service
@RequiredArgsConstructor
public class ClientCenterRightsService {

    private final ClientExternalRightsPort externalRightsService;

    @Cacheable(value = "clientRights", key = "#clientId", unless = "#result == null || #result.isEmpty()")
    @Transactional(readOnly = true)
    public Set<String> getClientRights(UUID clientId) {
        return externalRightsService.fetchClientRights(clientId);
    }

    @CachePut(value = "clientRights", key = "#clientId")
    @Transactional
    public Set<String> updateClientRights(UUID clientId, Set<String> newRights) {
        // Aquí simularemos la actualización en el servicio externo
        // En un caso real, llamarías al método correspondiente del puerto
        externalRightsService.updateClientRights(clientId, newRights);
        return newRights; // El valor retornado será el que se guarde en caché
    }

    @CacheEvict(value = "clientRights", key = "#clientId")
    public void invalidateClientRightsCache(UUID clientId) {
        // This method only invalidates the cache for the specified client
    }

    @CacheEvict(value = "clientRights", allEntries = true)
    public void invalidateAllClientRightsCache() {
        // This method invalidates all client rights cache entries
    }
}
