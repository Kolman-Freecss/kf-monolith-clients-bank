package org.kolmanfreecss.kf_monolith_clients_bank.domain.shared.valueobjects;

import java.time.LocalDate;

public record PersonalInformation(
    String firstName, String lastName, String documentId, DocumentType documentType,
    LocalDate dateOfBirth
) {

    public enum DocumentType {
        DNI, PASSPORT, RESIDENCE_CARD
    }

    public PersonalInformation {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (documentId == null || documentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Document ID cannot be empty");
        }
        if (documentType == null) {
            throw new IllegalArgumentException("Document type cannot be null");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
