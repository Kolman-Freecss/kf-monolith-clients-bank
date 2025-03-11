package org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.validators;

import java.time.LocalDateTime;
import java.util.Set;

import org.kolmanfreecss.kf_monolith_clients_bank.application._common.validator.ServiceValidator;
import org.kolmanfreecss.kf_monolith_clients_bank.application.client.usecases.permissions.models.ClientCenterRights;
import org.springframework.stereotype.Component;

/**
 * Validator implementation for ClientCenterRights operations.
 * This validator ensures that all ClientCenterRights data meets the required
 * business rules
 * and constraints before being processed by the service layer.
 *
 * Validation rules include:
 * - Non-null ClientCenterRights object
 * - Valid client ID
 * - Non-empty set of permissions
 * - Valid date constraints for rights validity
 *
 * P.D: (final class) This class is marked as final to prevent extension.
 *
 * @see ClientCenterRights
 * @see ServiceValidator
 * @author Kolman-Freecss
 * @version 1.0.0
 */
@Component
public final class ClientCenterRightsValidator implements ServiceValidator<ClientCenterRights> {

    /**
     * Validates a ClientCenterRights object according to business rules.
     * Performs comprehensive validation of all required fields and their
     * constraints.
     *
     * @param request The ClientCenterRights object to validate
     * @throws IllegalArgumentException if any validation constraint is violated
     */
    @Override
    public void validate(final ClientCenterRights request) {
        if (request == null) {
            throw new IllegalArgumentException("ClientCenterRights cannot be null");
        }

        if (request.getClientId() == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }

        validatePermissions(request.getPermissions());
        validateDates(request.getValidUntil());
    }

    /**
     * Determines if this validator can handle the specified request type.
     *
     * @param requestType The class type to check
     * @return true if the request type is assignable to ClientCenterRights, false
     *         otherwise
     */
    @Override
    public boolean canHandle(final Class<?> requestType) {
        return ClientCenterRights.class.isAssignableFrom(requestType);
    }

    /**
     * Validates the permissions set according to business rules.
     * Ensures that:
     * - The permissions set is not null or empty
     * - No permission string is blank or contains only whitespace
     *
     * @param permissions The set of permissions to validate
     * @throws IllegalArgumentException if permissions are invalid
     */
    private void validatePermissions(final Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException("Permissions cannot be null or empty");
        }

        // Validate that each permission is not blank
        if (permissions.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Permissions cannot contain blank values");
        }
    }

    /**
     * Validates the validity date for client rights.
     * Ensures that:
     * - The validity date is not null
     * - The validity date is in the future
     *
     * @param validUntil The date until which the rights are valid
     * @throws IllegalArgumentException if the date is invalid or in the past
     */
    private void validateDates(final LocalDateTime validUntil) {
        if (validUntil == null) {
            throw new IllegalArgumentException("Valid until date cannot be null");
        }

        if (validUntil.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Valid until date must be in the future");
        }
    }
}
