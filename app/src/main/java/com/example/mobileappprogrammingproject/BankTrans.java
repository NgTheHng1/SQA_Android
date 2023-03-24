package com.example.mobileappprogrammingproject;

import java.time.LocalDateTime;

public class BankTrans extends Transactions {
    private BankAccount bankAccount;


    public BankTrans(String transId, int amount, LocalDateTime time, int balanceAfter, BankAccount bankAccount, int transType) {
        super(transId, amount, time, balanceAfter, transType);
        this.bankAccount = bankAccount;
    }

    public BankTrans() {
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
