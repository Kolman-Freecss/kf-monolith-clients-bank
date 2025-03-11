package org.kolmanfreecss.kf_monolith_clients_bank.application._common.validator;

/**
 * Generic interface for service validators that perform validation before
 * executing service operations.
 * This interface provides a standard contract for implementing validation logic
 * across different services.
 * Validators implementing this interface should focus on business rules and
 * data integrity checks.
 *
 * Usage example:
 * {@code
 *     public class MyValidator implements ServiceValidator<MyData> {
 *         public void validate(final MyData data) {
 *             // Validation logic here
 * }
 * }
 * }
 *
 * @param <T> The type of the request/data to validate. This should be the
 *            domain model or DTO that needs validation.
 * @author Kolman-Freecss
 * @version 1.0.0
 */
public interface ServiceValidator<T> {

    /**
     * Validates the given request/data according to business rules and constraints.
     * This method should perform all necessary validation checks on the provided
     * data
     * and throw appropriate exceptions if the validation fails.
     *
     * @param request The request/data to validate
     * @throws IllegalArgumentException if validation fails due to invalid data
     * @throws NullPointerException     if required data is null
     */
    void validate(final T request);

    /**
     * Checks if this validator can handle the given request type.
     * This method is used to determine at runtime if a validator is capable
     * of handling a specific type of request/data.
     *
     * @param requestType The class of the request to check
     * @return true if this validator can handle the request type, false otherwise
     * @throws NullPointerException if requestType is null
     */
    boolean canHandle(final Class<?> requestType);
}
