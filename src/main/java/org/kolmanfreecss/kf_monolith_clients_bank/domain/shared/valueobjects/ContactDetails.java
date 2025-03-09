package org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects;

public record ContactDetails(
        String email,
        String phoneNumber,
        String alternativePhoneNumber) {
    public ContactDetails {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[0-9]{8,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (alternativePhoneNumber != null && !alternativePhoneNumber.isEmpty()
                && !alternativePhoneNumber.matches("^\\+?[0-9]{8,15}$")) {
            throw new IllegalArgumentException("Invalid alternative phone number format");
        }
    }
}