package org.banking.business;

import org.banking.enums.OperationType;
import org.banking.exceptions.BankAccountNotFoundException;
import org.banking.exceptions.InvalidAmountException;
import org.banking.exceptions.WithdrawalException;
import org.banking.helper.BankAccountHelper;
import org.banking.models.BankAccount;
import org.banking.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class AtmMachineTest {

    private BankAccount bankAccount;
    private User user;
    private AtmMachine atm;

    @BeforeEach
    void initializeBankAccount() {
        bankAccount = new BankAccount.BankAccountBuilder(100, "onepwd", "123").build();
        atm = new AtmMachine(BankAccountHelper.initializeBankAccounts());
        user = new User("onepwd", "123", "John Doe");
    }

    @Test
    void should_set_correct_bank_account_default_field_values() {
        // Given
        // When
        // Then
        Assertions.assertEquals(bankAccount.getMaxWithdrawal(), BankAccountHelper.DEFAULT_MAX_WITHDRAWAL);
        Assertions.assertEquals(bankAccount.getMinimumBalance(), BankAccountHelper.DEFAULT_MIN_BALANCE);
        assertTrue(bankAccount.getOperationsHistory().isEmpty());
    }

    @Test
    void should_throw_invalid_amount_exception_when_amount_less_than_one() {
        // Given
        atm.verifyAccessAndFindBankAccount(user);
        // Then
        Throwable exception = Assertions.assertThrows(InvalidAmountException.class, () -> {
                    // When
                    atm.withdraw(0.5);
                }
        );
        assertEquals("Withdrawal amount must be greater than 1.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_withdraw_more_than_allowed_value() {
        // Given
        atm.verifyAccessAndFindBankAccount(user);
        // Then
        Throwable exception = Assertions.assertThrows(WithdrawalException.class, () -> {
                    // When
                    atm.withdraw(2001);
                }
        );
        assertEquals("Your maximum withdrawal is 2 000,00", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_user_has_wrong_password() {
        // Given
        var user = new User("wrong_password", "123", "John Doe");
        Throwable exception = assertThrows(BankAccountNotFoundException.class, () -> {
                    // When
                    atm.verifyAccessAndFindBankAccount(user);
                }
        );
        assertEquals("Invalid Account Credentials.", exception.getMessage());
    }

    @Test
    void should_throw_exception_with_correct_error_message() {
        // Given
        var user = new User("twopwd", "001122", "Jennifer Aniston");
        atm.verifyAccessAndFindBankAccount(user);
        // Then
        Throwable exception = assertThrows(
                // When
                WithdrawalException.class, () -> {
                    atm.withdraw(601);
                }
        );
        assertEquals("You can not withdraw 601,00", exception.getMessage());
    }


    @Test
    void should_throw_exception_when_no_bank_account_loaded_in_atm() {
        // Given
        // Then
        Throwable exception = assertThrows(
                // When
                BankAccountNotFoundException.class, () -> {
                    atm.withdraw(601);
                }
        );
        assertEquals("No bank account yet loaded.", exception.getMessage());
    }

    @Test
    void should_set_correct_balance_value_and_correct_history() {
        // Given
        var user = new User("twopwd", "001122", "Jennifer Aniston");
        atm.verifyAccessAndFindBankAccount(user);

        // When
        atm.deposit(200);
        atm.withdraw(100);

        // Then
        assertEquals(600, atm.getLoadedBankAccount().getBalance());
        assertThat(atm.getLoadedBankAccount().getOperationsHistory())
                .hasSize(2)
                .extracting("operationType", "amount")
                .contains(tuple(OperationType.DEPOSIT, 200.0),
                        tuple(OperationType.WITHDRAWAL, 100.0));

    }


}