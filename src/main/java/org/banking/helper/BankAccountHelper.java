package org.banking.helper;

import org.banking.models.BankAccount;

import java.util.List;

public class BankAccountHelper {
    private BankAccountHelper() {
        throw new IllegalStateException("Utility Class");
    }

    public static final double DEFAULT_MAX_WITHDRAWAL = 1600.0;
    public static final double DEFAULT_MIN_BALANCE = -100.0;

    /**
     * @return List of mock bank accounts
     */
    public static List<BankAccount> initializeBankAccounts() {
        var bankAccount1 = new BankAccount.BankAccountBuilder(10500.90, "onepwd", "123")
                .minimumBalance(-200)
                .maxWithdrawal(2000)
                .build();
        var bankAccount2 = new BankAccount.BankAccountBuilder(500, "twopwd", "001122")
                .build();
        return List.of(bankAccount1, bankAccount2);
    }
}
