package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.ports;

import java.util.Set;
import java.util.UUID;

public interface ClientExternalRightsPort {
    Set<String> fetchClientRights(UUID clientId);
}