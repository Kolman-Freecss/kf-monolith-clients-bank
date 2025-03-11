package org.kolmanfreecss.kf_monolith_clients_bank.application.client.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services.ClientCenterRightsService;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.Client;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing client operations.
 * Metrics collection is handled separately through AOP. (see
 * {@link org.kolmanfreecss.kf_monolith_clients_bank.shared.metrics.aspects.ClientMetricsAspect})
 *
 * @author Kolman-Freecss
 * @version 1.0.0
 * @category Client
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientCenterRightsService clientCenterRightsService;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAllCached();
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientById(UUID id) {
        return Optional.ofNullable(clientRepository.findByIdCached(id));
    }

    @Transactional
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Optional<Client> updateClient(UUID id, Client updatedClient) {
        return clientRepository.findById(id).map(existingClient -> {
            existingClient.updateAddress(updatedClient.getAddress());
            existingClient.updateContactDetails(updatedClient.getContactDetails());
            existingClient.updateType(updatedClient.getType());
            return clientRepository.save(existingClient);
        });
    }

    @Transactional
    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Set<String> getClientRights(UUID clientId) {
        return clientCenterRightsService.getClientRights(clientId);
    }

    @Transactional
    public Set<String> updateClientRights(UUID clientId, Set<String> newRights) {
        return clientCenterRightsService.updateClientRights(clientId, newRights);
    }
}
