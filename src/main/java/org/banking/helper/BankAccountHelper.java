package org.banking.helper;

import org.banking.models.BankAccount;

import java.util.List;

public class BankAccountHelper {

    public static final double DEFAULT_MAX_WITHDRAWAL = 1600.0;
    public static final double DEFAULT_MIN_BALANCE = -100.0;
    public static List<BankAccount> initializeBankAccounts() {
        var bankAccount_1 = new BankAccount.BankAccountBuilder(10500.90, "onepwd", "123")
                .minimumBalance(-200)
                .maxWithdrawal(2000)
                .build();
        var bankAccount_2 = new BankAccount.BankAccountBuilder(500, "twopwd", "001122")
                .build();
        return List.of(bankAccount_1, bankAccount_2);
    }
}
