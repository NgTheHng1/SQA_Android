package com.example.mobileappprogrammingproject;

public class User {
    private String IdNum;
    private String fullName, phoneNum;

    public User(String idNum, String fullName, String phoneNum) {
        IdNum = idNum;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public User(String fullName, String phoneNum) {
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public String getIdNum() {
        return IdNum;
    }

    public void setIdNum(String idNum) {
        IdNum = idNum;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
