package org.kolmanfreecss.kf_monolith_clients_bank.domain.account.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Balance Value Object
 * 
 * Represents a monetary amount in a specific currency.
 * This is an immutable value object that ensures monetary values are always
 * properly formatted and handled with precision.
 *
 * Features:
 * - Immutable
 * - Always maintains 2 decimal places
 * - Validates currency and amount
 * - Provides safe arithmetic operations
 *
 * @version 1.0
 * @author Kolman-Freecss
 * @since 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA
public class Balance {
    private BigDecimal amount;
    private Currency currency;

    /**
     * Creates a new Balance with the specified amount and currency
     *
     * @param amount   The monetary amount
     * @param currency The currency of the amount
     * @throws IllegalArgumentException if amount or currency is null
     */
    public Balance(BigDecimal amount, Currency currency) {
        validateInputs(amount, currency);
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    /**
     * Adds an amount to this balance, creating a new Balance instance
     *
     * @param amount Amount to add
     * @return A new Balance with the sum
     */
    public Balance add(BigDecimal amount) {
        return new Balance(this.amount.add(amount), this.currency);
    }

    /**
     * Subtracts an amount from this balance, creating a new Balance instance
     *
     * @param amount Amount to subtract
     * @return A new Balance with the difference
     */
    public Balance subtract(BigDecimal amount) {
        return new Balance(this.amount.subtract(amount), this.currency);
    }

    /**
     * Checks if the balance is negative
     *
     * @return true if the amount is less than zero
     */
    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Compares this balance with another
     *
     * @param other Balance to compare with
     * @return true if this balance is less than the other
     * @throws IllegalArgumentException if currencies don't match
     */
    public boolean isLessThan(Balance other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    private void validateInputs(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
    }

    private void validateSameCurrency(Balance other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare balances with different currencies");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Balance balance = (Balance) o;
        return amount.compareTo(balance.amount) == 0 && currency.equals(balance.currency);
    }

    @Override
    public int hashCode() {
        return 31 * amount.hashCode() + currency.hashCode();
    }

    @Override
    public String toString() {
        return amount.toString() + " " + currency.getCurrencyCode();
    }
}