package org.banking.models;

import org.banking.enums.OperationType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operation {
    private OperationType operationType;
    private LocalDateTime timeStamp;
    private double amount;

    public Operation() {
        this.timeStamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timeStamp.format(formatter);
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
