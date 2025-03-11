package org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects;

/**
 * Value object representing contact information for a client.
 * Contains validation rules for email and phone numbers.
 * This is an immutable record that ensures all contact information is properly
 * formatted.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
public record ContactDetails(String email, String phoneNumber, String alternativePhoneNumber) {

    /**
     * Creates a new ContactDetails with validation.
     * Validates email format and phone number formats.
     *
     * @param email                  The email address (must be valid format)
     * @param phoneNumber            The primary phone number (8-15 digits, may
     *                               include + prefix)
     * @param alternativePhoneNumber Optional alternative phone number (same format
     *                               as primary)
     * @throws IllegalArgumentException if email or phone number formats are invalid
     */
    public ContactDetails {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[0-9]{8,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (alternativePhoneNumber != null && !alternativePhoneNumber.isEmpty() && !alternativePhoneNumber
                .matches("^\\+?[0-9]{8,15}$")) {
            throw new IllegalArgumentException("Invalid alternative phone number format");
        }
    }

}
