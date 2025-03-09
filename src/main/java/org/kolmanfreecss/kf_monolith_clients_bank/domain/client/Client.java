package org.kolmanfreecss.kf_monolith_clients_bank.domain.client;

import java.time.LocalDateTime;
import java.util.UUID;

import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.vo.ClientType;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects.Address;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects.ContactDetails;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects.PersonalInformation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Client Aggregate Root Entity
 * 
 * This class represents a bank client and acts as the aggregate root for the
 * Client bounded context.
 * It encapsulates all client-related information and enforces business rules
 * regarding client status
 * and verification processes.
 * 
 * The client aggregate includes:
 * - Personal Information (Value Object)
 * - Address (Value Object)
 * - Contact Details (Value Object)
 * - Client Status (Entity)
 * - Client Type (Value Object)
 *
 * @version 1.0
 * @author Kolman-Freecss
 * @since 1.0
 */
@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
public class Client {
    @Id
    private UUID id;

    @Embedded
    private PersonalInformation personalInformation;

    @Embedded
    private Address address;

    @Embedded
    private ContactDetails contactDetails;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "status_id")
    private ClientStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Version field for optimistic locking
     */
    @Version
    private Long version;

    /**
     * Creates a new Client with the provided information.
     * The client starts in PENDING_VERIFICATION status.
     *
     * @param personalInformation The client's personal information
     * @param address             The client's address
     * @param contactDetails      The client's contact details
     * @param type                The client's type (INDIVIDUAL, BUSINESS, VIP)
     */
    public Client(
            PersonalInformation personalInformation,
            Address address,
            ContactDetails contactDetails,
            ClientType type) {
        this.id = UUID.randomUUID();
        this.personalInformation = personalInformation;
        this.address = address;
        this.contactDetails = contactDetails;
        this.type = type;
        this.status = new ClientStatus(ClientStatus.Status.PENDING_VERIFICATION, "New client registration");
        this.createdAt = LocalDateTime.now();
    }

    // Domain methods

    /**
     * Updates the client's address
     *
     * @param newAddress The new address to set
     */
    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }

    /**
     * Updates the client's contact details
     *
     * @param newContactDetails The new contact details to set
     */
    public void updateContactDetails(ContactDetails newContactDetails) {
        this.contactDetails = newContactDetails;
    }

    /**
     * Updates the client's type
     *
     * @param newType The new client type to set
     */
    public void updateType(ClientType newType) {
        this.type = newType;
    }

    /**
     * Verifies the client, changing their status from PENDING_VERIFICATION to
     * ACTIVE
     *
     * @throws IllegalStateException if the client is not in PENDING_VERIFICATION
     *                               status
     */
    public void verify() {
        if (status.getStatus() != ClientStatus.Status.PENDING_VERIFICATION) {
            throw new IllegalStateException("Client is not pending verification");
        }
        status.updateStatus(ClientStatus.Status.ACTIVE, "Client verified successfully");
    }

    /**
     * Suspends the client's account
     *
     * @param reason The reason for suspension
     * @throws IllegalStateException if the client is already suspended
     */
    public void suspend(String reason) {
        if (status.getStatus() == ClientStatus.Status.SUSPENDED) {
            throw new IllegalStateException("Client is already suspended");
        }
        status.updateStatus(ClientStatus.Status.SUSPENDED, reason);
    }

    /**
     * Blocks the client's account
     *
     * @param reason The reason for blocking
     * @throws IllegalStateException if the client is already blocked
     */
    public void block(String reason) {
        if (status.getStatus() == ClientStatus.Status.BLOCKED) {
            throw new IllegalStateException("Client is already blocked");
        }
        status.updateStatus(ClientStatus.Status.BLOCKED, reason);
    }

    /**
     * Activates the client's account
     *
     * @param reason The reason for activation
     * @throws IllegalStateException if the client is already active
     */
    public void activate(String reason) {
        if (status.getStatus() == ClientStatus.Status.ACTIVE) {
            throw new IllegalStateException("Client is already active");
        }
        status.updateStatus(ClientStatus.Status.ACTIVE, reason);
    }
}