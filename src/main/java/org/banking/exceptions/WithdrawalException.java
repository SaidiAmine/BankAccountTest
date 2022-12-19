package org.banking.exceptions;

public class WithdrawalException extends RuntimeException {
    public WithdrawalException(String message) {
        super(message);
    }
}
