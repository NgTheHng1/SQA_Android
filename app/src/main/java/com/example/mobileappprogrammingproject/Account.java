package com.example.mobileappprogrammingproject;

public class Account {
    private String phoneNum, mail;
    private int balance;
    private UserTrans user;

    public Account(String phoneNum, String mail, int balance, UserTrans user) {
        this.phoneNum = phoneNum;
        this.mail = mail;
        this.balance = balance;
        this.user = user;
    }

    public Account(String phoneNum, String mail, UserTrans user) {
        this.phoneNum = phoneNum;
        this.mail = mail;
        this.user = user;
    }

    public Account(String phoneNum, UserTrans user) {
        this.phoneNum = phoneNum;
        this.user = user;
    }

    public Account() {
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getBlance() {
        return balance;
    }

    public void setBlance(int balance) {
        this.balance = balance;
    }

    public UserTrans getUser() {
        return user;
    }

    public void setUser(UserTrans user) {
        this.user = user;
    }
}
