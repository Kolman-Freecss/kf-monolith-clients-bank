package org.kolmanfreecss.kf_monolith_clients_bank.application.client.services;

import lombok.RequiredArgsConstructor;
import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services.ClientCenterRightsService;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.Client;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * ClientService
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
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Optional<Client> updateClient(UUID id, Client updatedClient) {
        return clientRepository.findById(id)
            .map(existingClient -> {
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
        // First verify if client exists
        if (!clientRepository.existsById(clientId)) {
            throw new IllegalArgumentException("Client not found with id: " + clientId);
        }
        return clientCenterRightsService.getClientRights(clientId);
    }
}
