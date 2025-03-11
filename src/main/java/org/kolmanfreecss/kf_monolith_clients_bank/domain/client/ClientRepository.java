package org.kolmanfreecss.kf_monolith_clients_bank.domain.client;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Client entity operations.
 * Provides CRUD operations and custom queries for client management.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

}
