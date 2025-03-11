package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.adapters.out.external;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports.ClientExternalRightsPort;
import org.springframework.stereotype.Service;

/**
 * Mock implementation of an external rights management service.
 * Uses a static ConcurrentHashMap to simulate a persistent storage.
 *
 * Example of internal storage structure:
 * {
 * "123e4567-e89b-12d3-a456-426614174000": ["VIEW_ACCOUNT", "MAKE_TRANSFER",
 * "VIEW_STATEMENTS"],
 * "987fcdeb-51a2-43f7-9876-543210abcdef": ["VIEW_ACCOUNT", "VIEW_STATEMENTS",
 * "ADMIN_ACCESS"]
 * }
 *
 * The storage persists for the lifetime of the JVM, simulating an external
 * database.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Service
public class ClientExternalRightsAdapter implements ClientExternalRightsPort {

    // Simulating an external database with a static Map
    // Key: Client UUID (the one provided in the request)
    // Value: Set of rights associated with that client
    private static final Map<UUID, Set<String>> EXTERNAL_RIGHTS_DB = new ConcurrentHashMap<>();

    @Override
    public Set<String> fetchClientRights(UUID clientId) {
        // If the client doesn't exist in our "database", create default rights
        // Example: fetchClientRights("123e4567-e89b-12d3-a456-426614174000")
        // Returns: ["VIEW_ACCOUNT", "MAKE_TRANSFER", "VIEW_STATEMENTS"]
        return EXTERNAL_RIGHTS_DB.computeIfAbsent(clientId, k -> {
            Set<String> defaultRights = new HashSet<>();
            defaultRights.add("VIEW_ACCOUNT");
            defaultRights.add("MAKE_TRANSFER");
            defaultRights.add("VIEW_STATEMENTS");
            return defaultRights;
        });
    }

    @Override
    public Set<String> updateClientRights(UUID clientId, Set<String> newRights) {
        // Update the rights in our "database"
        // Example: updateClientRights("123e4567-e89b-12d3-a456-426614174000",
        // ["VIEW_ACCOUNT", "ADMIN_ACCESS"])
        // Updates the database and returns: ["VIEW_ACCOUNT", "ADMIN_ACCESS"]
        EXTERNAL_RIGHTS_DB.put(clientId, new HashSet<>(newRights));
        return newRights;
    }

}
