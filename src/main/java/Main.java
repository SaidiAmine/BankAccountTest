import org.banking.business.AtmMachine;
import org.banking.exceptions.BankAccountNotFoundException;
import org.banking.exceptions.InvalidAmountException;
import org.banking.exceptions.WithdrawalException;
import org.banking.helper.BankAccountHelper;
import org.banking.models.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        User user_1 = new User("onepwd", "123", "John Doe");
        AtmMachine atmMachine = new AtmMachine(BankAccountHelper.initializeBankAccounts());
        atmMachine.verifyAccessAndFindBankAccount(user_1);
        try {
        // atmMachine.withdraw(2500);
        //atmMachine.deposit(0.5);
        atmMachine.deposit(400.58);
        atmMachine.withdraw(300);
        } catch (BankAccountNotFoundException | InvalidAmountException | WithdrawalException e) {
            System.out.println(e.getMessage());
        }
        atmMachine.displayHistory();
        System.out.println("OK");
    }
}