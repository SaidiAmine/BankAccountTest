package org.banking.models;


import java.util.ArrayList;
import java.util.List;

import static org.banking.helper.BankAccountHelper.DEFAULT_MAX_WITHDRAWAL;
import static org.banking.helper.BankAccountHelper.DEFAULT_MIN_BALANCE;

public class BankAccount {

    private Double balance;
    private final String password;
    private final String rib;
    // Optional fields
    private Double maxWithdrawal;
    private Double minimumBalance;
    private List<Operation> operationsHistory;

    public BankAccount(BankAccountBuilder builder) {
        this.balance = builder.balance;
        this.password = builder.password;
        this.maxWithdrawal = builder.maxWithdrawal;
        this.minimumBalance = builder.minimumBalance;
        this.rib = builder.rib;
        this.operationsHistory = builder.operationsHistory;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getMaxWithdrawal() {
        return maxWithdrawal;
    }

    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public String getRib() {
        return rib;
    }

    public String getPassword() {
        return password;
    }

    public List<Operation> getOperationsHistory() {
        return operationsHistory;
    }

    public static class BankAccountBuilder {
        // Required fields
        private double balance;
        private String password;
        private String rib;
        private List<Operation> operationsHistory;
        // Optional fields
        private double maxWithdrawal;
        private double minimumBalance;


        public BankAccountBuilder(double balance, String password, String rib) {
            this.balance = balance;
            this.password = password;
            this.rib = rib;
            this.operationsHistory = new ArrayList<>();
            this.maxWithdrawal = DEFAULT_MAX_WITHDRAWAL; // default bank values
            this.minimumBalance = DEFAULT_MIN_BALANCE;
        }

        public BankAccountBuilder maxWithdrawal(double maxWithdrawal) {
            this.maxWithdrawal = maxWithdrawal;
            return this;
        }

        public BankAccountBuilder minimumBalance(double minimumBalance) {
            this.minimumBalance = minimumBalance;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(this);
        }
    }
}
