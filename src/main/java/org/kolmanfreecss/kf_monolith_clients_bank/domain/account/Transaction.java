package org.kolmanfreecss.kf_monolith_clients_bank.domain.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a financial transaction in the system.
 * Records all movements of money within accounts, including deposits,
 * withdrawals, transfers, fees, and interest payments.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String description;

    @Column(nullable = false)
    private BigDecimal balanceAfterTransaction;

    /**
     * Enumeration of possible transaction types.
     */
    public enum TransactionType {
        /** Money added to account */
        DEPOSIT,
        /** Money removed from account */
        WITHDRAWAL,
        /** Money received from another account */
        TRANSFER_IN,
        /** Money sent to another account */
        TRANSFER_OUT,
        /** Service or maintenance fee */
        FEE,
        /** Interest earned on balance */
        INTEREST
    }

    protected Transaction() {
    } // JPA

    /**
     * Creates a new transaction with the specified details.
     *
     * @param accountId               The ID of the account this transaction belongs
     *                                to
     * @param type                    The type of transaction
     * @param amount                  The monetary amount of the transaction
     * @param currency                The currency of the transaction
     * @param description             A description of the transaction
     * @param balanceAfterTransaction The account balance after this transaction
     */
    public Transaction(
            UUID accountId,
            TransactionType type,
            BigDecimal amount,
            Currency currency,
            String description,
            BigDecimal balanceAfterTransaction) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    /**
     * @return The unique identifier of this transaction
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The ID of the account this transaction belongs to
     */
    public UUID getAccountId() {
        return accountId;
    }

    /**
     * @return The type of this transaction
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * @return The monetary amount of this transaction
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return The currency of this transaction
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @return The timestamp when this transaction occurred
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @return The description of this transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The account balance after this transaction was applied
     */
    public BigDecimal getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
}
