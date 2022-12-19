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
    private final BiPredicate<User, BankAccount> isUserAllowedToAccessBankAccount = ((user, bankAccount)
            -> user.getRib().equals(bankAccount.getRib()) && user.getPassword().equals(bankAccount.getPassword()));

    public AtmMachine(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void verifyAccessAndFindBankAccount(User user) {
        loadedBankAccount = bankAccounts.stream()
                .filter(bankAccount -> isUserAllowedToAccessBankAccount.test(user, bankAccount))
                .findAny()
                .orElseThrow(() -> new BankAccountNotFoundException("Invalid Account Credentials."));
    }

    public void withdraw(double amount) {
        if(loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        if(amount<1)
            throw new InvalidAmountException("Withdrawal amount must be greater than 1.");
        if(loadedBankAccount.getBalance()-amount<loadedBankAccount.getMinimumBalance())
            throw new WithdrawalException(String.format("You can not withdraw %,.2f", amount));
        if(amount> loadedBankAccount.getMaxWithdrawal())
            throw new WithdrawalException(String.format("Your maximum withdrawal is %,.2f", loadedBankAccount.getMaxWithdrawal()));
        saveOperation(OperationType.WITHDRAWAL, amount);
        loadedBankAccount.setBalance(loadedBankAccount.getBalance()-amount);
    }

    public void deposit(double amount) {
        if(loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        if(amount<1)
            throw new InvalidAmountException("Deposit amount must be superior to 1.");
        saveOperation(OperationType.DEPOSIT, amount);
        loadedBankAccount.setBalance(loadedBankAccount.getBalance()+amount);
    }

    private void saveOperation(OperationType type, double amount) {
        if(loadedBankAccount == null)
            throw new BankAccountNotFoundException("No bank account yet loaded.");
        Operation operation = new Operation();
        operation.setOperationType(type);
        operation.setAmount(amount);
        loadedBankAccount.getOperationsHistory().add(operation);
    }

    public BankAccount getLoadedBankAccount() {
        return loadedBankAccount;
    }

    public void displayHistory() {
        if(loadedBankAccount!=null) {
            StringBuilder builder = new StringBuilder();
            builder.append("Balance: \n");
            //Iterator<Operation> itr = loadedBankAccount.getOperationsHistory().iterator();
            //ReverseListIterator reverseListIterator = new ReverseListIterator(list);
            ListIterator<Operation> listIterator = loadedBankAccount.getOperationsHistory().listIterator(loadedBankAccount.getOperationsHistory().size());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (listIterator.hasPrevious()) {
                var element = listIterator.previous();

                builder.append(element.getTimeStamp().format(formatter))
                        .append(" - ")
                        .append(element.getOperationType())
                        .append(" - ")
                        .append(element.getAmount())
                        .append("\n");            }

            System.out.println(builder);
        }
    }

}
