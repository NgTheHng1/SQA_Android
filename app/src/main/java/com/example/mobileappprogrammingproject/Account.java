package com.example.mobileappprogrammingproject;

public class Account {
    private String phoneNum, mail;
    private int balance;
    private User user;

    public Account(String phoneNum, String mail, int balance, User user) {
        this.phoneNum = phoneNum;
        this.mail = mail;
        this.balance = balance;
        this.user = user;
    }

    public Account(String phoneNum, String mail, User user) {
        this.phoneNum = phoneNum;
        this.mail = mail;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
