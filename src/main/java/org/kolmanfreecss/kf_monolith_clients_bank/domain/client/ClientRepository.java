package org.kolmanfreecss.kf_monolith_clients_bank.domain.client;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;

/**
 * Repository interface for Client entity operations.
 * Provides CRUD operations and custom queries for client management.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Query("SELECT c FROM Client c")
    List<Client> findAllCached();

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Query("SELECT c FROM Client c WHERE c.id = ?1")
    Client findByIdCached(UUID id);
}
