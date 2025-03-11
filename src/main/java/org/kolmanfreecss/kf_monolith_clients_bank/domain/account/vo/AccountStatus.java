package org.kolmanfreecss.kf_monolith_clients_bank.domain.account.vo;

/**
 * Enumeration representing the possible states of a bank account.
 * Each status determines what operations are allowed on the account.
 *
 * <p>
 * Available statuses:
 * <ul>
 * <li>ACTIVE - Normal operational state, all operations allowed</li>
 * <li>FROZEN - Limited operations, only deposits allowed</li>
 * <li>CLOSED - No operations allowed</li>
 * </ul>
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
public enum AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED;

    /**
     * Checks if withdrawals are allowed for this account status.
     *
     * @return true if withdrawals are allowed, false otherwise
     */
    public boolean canWithdraw() {
        return this == ACTIVE;
    }

    /**
     * Checks if deposits are allowed for this account status.
     *
     * @return true if deposits are allowed, false otherwise
     */
    public boolean canDeposit() {
        return this == ACTIVE || this == FROZEN;
    }

    /**
     * Checks if the account can be closed from this status.
     *
     * @return true if the account can be closed, false otherwise
     */
    public boolean canClose() {
        return this != CLOSED;
    }
}