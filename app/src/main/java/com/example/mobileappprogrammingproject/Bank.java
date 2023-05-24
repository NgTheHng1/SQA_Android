package com.example.mobileappprogrammingproject;

public class Bank {
    private int bankId;
    private double bankFee;
    private String bankName;
    private int sourceImg;

    public Bank(int bankId, double bankFee, String bankName, int sourceImg) {
        this.bankId = bankId;
        this.bankFee = bankFee;
        this.bankName = bankName;
        this.sourceImg = sourceImg;
    }

    public Bank(String bankName) {
        this.bankName = bankName;
    }

    public Bank() {
    }

    public static int[] getBankSourceImg(){
        return new int[]{
                0,
                R.drawable.sacombank_logo,
                R.drawable.vietinbank_logo,
                R.drawable.agribank_logo,
                R.drawable.techcombank_logo
        };
    }

    public int getSourceImg() {
        return sourceImg;
    }

    public void setSourceImg(int sourceImg) {
        this.sourceImg = sourceImg;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public double getBankFee() {
        return bankFee;
    }

    public void setBankFee(int bankFee) {
        this.bankFee = bankFee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
