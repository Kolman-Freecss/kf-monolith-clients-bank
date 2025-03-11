package org.kolmanfreecss.kf_monolith_clients_bank.application.client.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.services.ClientCenterRightsService;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.Client;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.ClientRepository;
import org.kolmanfreecss.kf_monolith_clients_bank.shared.metrics.ClientMetrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing client operations with integrated metrics
 * tracking.
 * Each operation is monitored for:
 * - Execution time (using Timer.Sample)
 * - Operation counts (using counters)
 * - Active clients count (using gauge)
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
    private final ClientMetrics metrics;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        // Start timing the operation to measure its duration
        Timer.Sample sample = metrics.startTimer();
        try {
            List<Client> clients = clientRepository.findAllCached();
            // Update the gauge with the current number of active clients
            metrics.setActiveClients(clients.size());
            return clients;
        } finally {
            // Record the operation duration in our metrics
            metrics.stopTimer(sample, "get_all_clients");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientById(UUID id) {
        // Measure the time taken to fetch a single client
        Timer.Sample sample = metrics.startTimer();
        try {
            return Optional.ofNullable(clientRepository.findByIdCached(id));
        } finally {
            // Record the operation duration for monitoring performance
            metrics.stopTimer(sample, "get_client_by_id");
        }
    }

    @Transactional
    public Client createClient(Client client) {
        // Track the duration of client creation process
        Timer.Sample sample = metrics.startTimer();
        try {
            Client savedClient = clientRepository.save(client);
            // Increment the counter tracking total number of clients created
            metrics.incrementClientCreation();
            return savedClient;
        } finally {
            // Record how long the creation process took
            metrics.stopTimer(sample, "create_client");
        }
    }

    @Transactional
    public Optional<Client> updateClient(UUID id, Client updatedClient) {
        // Measure update operation duration
        Timer.Sample sample = metrics.startTimer();
        try {
            Optional<Client> result = clientRepository.findById(id).map(existingClient -> {
                existingClient.updateAddress(updatedClient.getAddress());
                existingClient.updateContactDetails(updatedClient.getContactDetails());
                existingClient.updateType(updatedClient.getType());
                return clientRepository.save(existingClient);
            });
            // Only increment the update counter if the client was actually updated
            if (result.isPresent()) {
                metrics.incrementClientUpdate();
            }
            return result;
        } finally {
            // Record the total time taken for the update operation
            metrics.stopTimer(sample, "update_client");
        }
    }

    @Transactional
    public void deleteClient(UUID id) {
        // Track deletion operation duration
        Timer.Sample sample = metrics.startTimer();
        try {
            clientRepository.deleteById(id);
            // Increment the counter for client deletions
            metrics.incrementClientDeletion();
        } finally {
            // Record how long the deletion took
            metrics.stopTimer(sample, "delete_client");
        }
    }

    @Transactional(readOnly = true)
    public Set<String> getClientRights(UUID clientId) {
        // Measure time taken to retrieve client rights
        Timer.Sample sample = metrics.startTimer();
        try {
            return clientCenterRightsService.getClientRights(clientId);
        } finally {
            // Record the duration of rights retrieval
            metrics.stopTimer(sample, "get_client_rights");
        }
    }

    @Transactional
    public Set<String> updateClientRights(UUID clientId, Set<String> newRights) {
        // Track the duration of rights update operation
        Timer.Sample sample = metrics.startTimer();
        try {
            Set<String> result = clientCenterRightsService.updateClientRights(clientId, newRights);
            // Increment counter for rights updates
            metrics.incrementClientRightsUpdate();
            return result;
        } finally {
            // Record the time taken to update rights
            metrics.stopTimer(sample, "update_client_rights");
        }
    }
}
