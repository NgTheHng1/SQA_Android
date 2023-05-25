package com.example.mobileappprogrammingproject;

public class UserTrans {
    private String IdNum;
    private String fullName, phoneNum;

    public UserTrans(String idNum, String fullName, String phoneNum) {
        IdNum = idNum;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public UserTrans(String fullName, String phoneNum) {
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public UserTrans(String fullName) {
        this.fullName = fullName;
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
