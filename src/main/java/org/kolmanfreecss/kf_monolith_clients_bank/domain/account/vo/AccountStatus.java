package org.kolmanfreecss.kf_monolith_clients_bank.domain.account.vo;

public enum AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED;

    public boolean canWithdraw() {
        return this == ACTIVE;
    }

    public boolean canDeposit() {
        return this == ACTIVE || this == FROZEN;
    }

    public boolean canClose() {
        return this != CLOSED;
    }
}