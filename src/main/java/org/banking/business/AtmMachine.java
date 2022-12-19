package org.banking.business;

import org.banking.enums.OperationType;
import org.banking.exceptions.BankAccountNotFoundException;
import org.banking.exceptions.InvalidAmountException;
import org.banking.exceptions.WithdrawalException;
import org.banking.models.BankAccount;
import org.banking.models.Operation;
import org.banking.models.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiPredicate;

public class AtmMachine {

    private List<BankAccount> bankAccounts;
    private BankAccount loadedBankAccount;
    /**
     * BiPredicate to test bank account belongs to user
     */
    private final BiPredicate<User, BankAccount> isUserAllowedToAccessBankAccount = ((user, bankAccount)
            -> user.getRib().equals(bankAccount.getRib()) && user.getPassword().equals(bankAccount.getPassword()));

    public AtmMachine(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
    public BankAccount getLoadedBankAccount() {
        return loadedBankAccount;
    }


    /**
     * @param user The user which account should be loaded,
     *             tests if the user credentials match any of the bank accounts
     *             and initializes the loadedBankAccount field.
     */
    public void verifyAccessAndFindBankAccount(User user) {
        loadedBankAccount = bankAccounts.stream()
                .filter(bankAccount -> isUserAllowedToAccessBankAccount.test(user, bankAccount))
                .findAny()
                .orElseThrow(() -> new BankAccountNotFoundException("Invalid Account Credentials."));
    }

    /**
     * @param amount The amount to withdraw by the user from the bank account
     * @throws BankAccountNotFoundException if bank account not loaded
     * @throws InvalidAmountException       if amount to withdraw is less than 1
     * @throws WithdrawalException          if balance is less than the account's minimum or amount is more than the maximum withdrawal
     */
    public void withdraw(double amount) {
        if (loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        if (amount < 1)
            throw new InvalidAmountException("Withdrawal amount must be greater than 1.");
        if (loadedBankAccount.getBalance() - amount < loadedBankAccount.getMinimumBalance())
            throw new WithdrawalException(String.format("Your minimum balance is %,.2f", loadedBankAccount.getMinimumBalance()));
        if (amount > loadedBankAccount.getMaxWithdrawal())
            throw new WithdrawalException(String.format("Your maximum withdrawal is %,.2f", loadedBankAccount.getMaxWithdrawal()));
        saveOperation(OperationType.WITHDRAWAL, amount);
        loadedBankAccount.setBalance(loadedBankAccount.getBalance() - amount);
    }

    /**
     * @param amount The amount to deposit by the user to the bank account
     * @throws BankAccountNotFoundException if bank account not loaded
     * @throws InvalidAmountException       if amount to deposit is less than 1
     */
    public void deposit(double amount) {
        if (loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        if (amount < 1)
            throw new InvalidAmountException("Deposit amount must be superior to 1.");
        saveOperation(OperationType.DEPOSIT, amount);
        loadedBankAccount.setBalance(loadedBankAccount.getBalance() + amount);
    }


    /**
     * Saves the operation in the bank account's history
     *
     * @param type   The operation type
     * @param amount The amount used in the operation
     * @throws BankAccountNotFoundException if bank account not loaded
     */
    private void saveOperation(OperationType type, double amount) {
        if (loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        Operation operation = new Operation();
        operation.setOperationType(type);
        operation.setAmount(amount);
        loadedBankAccount.getOperationsHistory().add(operation);
    }

    /**
     * Displays the bank account's history using StringBuilder
     */
    public void displayHistory() {
        if (loadedBankAccount != null) {
            StringBuilder builder = new StringBuilder();
            var history = loadedBankAccount.getOperationsHistory();
            builder.append("Balance: \n");
            ListIterator<Operation> listIterator = history.listIterator(history.size());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (listIterator.hasPrevious()) {
                var element = listIterator.previous();
                builder.append(element.getTimeStamp().format(formatter)) // Reformat timestamp
                        .append(" - ")
                        .append(element.getOperationType())
                        .append(" - ")
                        .append(element.getAmount())
                        .append("\n");
            }
            System.out.println(builder);
        }
    }

}
