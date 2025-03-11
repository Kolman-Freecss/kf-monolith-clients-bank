package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports;

import java.util.Set;
import java.util.UUID;

/**
 * Port interface for external client rights management.
 * Defines operations for interacting with external rights management systems.
 * This interface follows the ports and adapters pattern, allowing for different
 * implementations of rights management systems.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
public interface ClientExternalRightsPort {

    /**
     * Fetches the rights for a specific client from the external system.
     *
     * @param clientId The UUID of the client
     * @return Set of rights associated with the client
     */
    Set<String> fetchClientRights(UUID clientId);

    /**
     * Updates the rights for a specific client in the external system.
     *
     * @param clientId  The UUID of the client
     * @param newRights The new set of rights to assign
     * @return Updated set of rights
     */
    Set<String> updateClientRights(UUID clientId, Set<String> newRights);

}
