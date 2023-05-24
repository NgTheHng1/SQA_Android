package com.example.mobileappprogrammingproject;

public class BankAccount {
    private String bankAccId;
    int balance;
    private String phoneNum;
    private Bank bank;
    private int sourceImg;

    public BankAccount(String bankAccId, int balance, String phoneNum, Bank bank) {
        this.bankAccId = bankAccId;
        this.balance = balance;
        this.phoneNum = phoneNum;
        this.bank = bank;
    }

    public BankAccount(Bank bank){
        this.bank = bank;
    }

    public BankAccount() {
    }

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
