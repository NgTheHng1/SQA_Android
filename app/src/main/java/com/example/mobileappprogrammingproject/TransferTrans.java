package com.example.mobileappprogrammingproject;

import java.time.LocalDateTime;

public class TransferTrans extends Transactions {
    private Account oppoPerson;
    private String message;

    private int fee;

    public TransferTrans(String transId, int amount, LocalDateTime time, int balanceAfter, Account oppoPerson, int transType) {
        super(transId, amount, time, balanceAfter, transType);
        this.oppoPerson = oppoPerson;
    }

    public TransferTrans(String transId, int amount, LocalDateTime time, int balanceAfter, Account oppoPerson, int transType, String message) {
        super(transId, amount, time, balanceAfter, transType);
        this.oppoPerson = oppoPerson;
        this.message = message;
    }

    public TransferTrans(String transId, int amount, int fee, LocalDateTime time, int balanceAfter, Account oppoPerson, int transType, String message) {
        super(transId, amount, time, balanceAfter, transType);
        this.oppoPerson = oppoPerson;
        this.message = message;
        this.fee = fee;
    }

    public TransferTrans() {
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getOppoPerson() {
        return oppoPerson;
    }

    public void setOppoPerson(Account oppoPerson) {
        this.oppoPerson = oppoPerson;
    }
}
