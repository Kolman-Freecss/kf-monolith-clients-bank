package org.kolmanfreecss.kf_monolith_clients_bank.domain.client;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Client Status Entity
 * <p>
 * Represents the current status of a client in the system. This entity is part
 * of the Client aggregate and tracks the client's lifecycle through different
 * states (pending verification, active, suspended, blocked).
 * <p>
 * Each status change is recorded with a timestamp and reason for the change.
 *
 * @author Kolman-Freecss
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "client_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientStatus {

    @Id
    private Long id;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column
    private String reason;

    /**
     * Possible states a client can be in
     */
    public enum Status {
        /**
         * Initial state when client is created
         */
        PENDING_VERIFICATION,
        /**
         * Normal operational state
         */
        ACTIVE,
        /**
         * Temporarily restricted state
         */
        SUSPENDED,
        /**
         * Permanently restricted state
         */
        BLOCKED
    }

    /**
     * Creates a new ClientStatus with the specified status and reason
     *
     * @param status Initial status of the client
     * @param reason Reason for setting this status
     */
    public ClientStatus(Status status, String reason) {
        this.status = status;
        this.reason = reason;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Updates the client's status with a new status and reason
     *
     * @param newStatus New status to set
     * @param reason    Reason for the status change
     */
    public void updateStatus(Status newStatus, String reason) {
        this.status = newStatus;
        this.reason = reason;
        this.lastUpdated = LocalDateTime.now();
    }
}
