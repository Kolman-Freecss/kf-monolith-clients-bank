package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.models;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;

/**
 * ClientCenterRights Service Model
 * 
 * This class represents the rights and permissions a client has in the system.
 * It is used in the application layer to manage client permissions and rights.
 */
@Getter
public class ClientCenterRights {
    private final UUID id;
    private final UUID clientId;
    private Set<String> permissions;
    private LocalDateTime lastUpdated;
    private LocalDateTime validUntil;
    private boolean isActive;

    public ClientCenterRights(UUID clientId, Set<String> permissions, LocalDateTime validUntil) {
        this.id = UUID.randomUUID();
        this.clientId = clientId;
        this.permissions = permissions;
        this.lastUpdated = LocalDateTime.now();
        this.validUntil = validUntil;
        this.isActive = true;
    }

    public void updatePermissions(Set<String> newPermissions) {
        this.permissions = newPermissions;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.lastUpdated = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.lastUpdated = LocalDateTime.now();
    }

    public void extendValidity(LocalDateTime newValidUntil) {
        if (newValidUntil.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("New validity date must be in the future");
        }
        this.validUntil = newValidUntil;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean hasPermission(String permission) {
        return isActive &&
                LocalDateTime.now().isBefore(validUntil) &&
                permissions.contains(permission);
    }
}