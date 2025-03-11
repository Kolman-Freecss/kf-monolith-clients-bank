package org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects;

/**
 * Value object representing a physical address.
 * Contains validation rules for all address components.
 * This is an immutable record that ensures all address fields are properly
 * validated.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
public record Address(String street, String city, String state, String country, String postalCode) {

    /**
     * Creates a new Address with validation.
     * Throws IllegalArgumentException if any field is null or empty.
     *
     * @param street     The street address including number
     * @param city       The city name
     * @param state      The state or province
     * @param country    The country name
     * @param postalCode The postal or ZIP code
     * @throws IllegalArgumentException if any field is null or empty
     */
    public Address {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code cannot be empty");
        }
    }

}
