package com.example.mobileappprogrammingproject;

import java.time.LocalDateTime;

public class BankTrans extends Transactions {
    private BankAccount bankAccount;
    private int fee;


    public BankTrans(String transId, int amount, LocalDateTime time, int balanceAfter, BankAccount bankAccount, int transType) {
        super(transId, amount, time, balanceAfter, transType);
        this.bankAccount = bankAccount;
    }

    public BankTrans(String transId, int amount, LocalDateTime time, int balanceAfter, BankAccount bankAccount, int transType, int fee) {
        super(transId, amount, time, balanceAfter, transType);
        this.bankAccount = bankAccount;
        this.fee = fee;
    }

    public BankTrans() {
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
