package org.kolmanfreecss.kf_monolith_clients_bank.domain.client.vo;

public enum ClientType {
    INDIVIDUAL,
    BUSINESS,
    VIP;

    public boolean canHaveMultipleAccounts() {
        return this != INDIVIDUAL;
    }

    public int getMaxAccounts() {
        return switch (this) {
            case INDIVIDUAL -> 3;
            case BUSINESS -> 10;
            case VIP -> 15;
        };
    }

    public double getMonthlyMaintenanceFee() {
        return switch (this) {
            case INDIVIDUAL -> 5.0;
            case BUSINESS -> 15.0;
            case VIP -> 0.0;
        };
    }
}