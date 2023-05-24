package com.example.mobileappprogrammingproject;

import java.time.LocalDateTime;

public class BillTrans extends Transactions{
    private String billName, comName, address;
    private LocalDateTime dueDate;
    private String cusName, cusId;

    public BillTrans(String transId, int amount, LocalDateTime time, int balanceAfter, int transType, String billName, String comName, String address, LocalDateTime dueDate, String cusName) {
        super(transId, amount, time, balanceAfter, transType);
        this.billName = billName;
        this.comName = comName;
        this.address = address;
        this.dueDate = dueDate;
        this.cusName = cusName;
    }

    public BillTrans(String transId, int amount, LocalDateTime time, int balanceAfter, int transType, String billName, String comName, String address, LocalDateTime dueDate, String cusName, String cusId) {
        super(transId, amount, time, balanceAfter, transType);
        this.billName = billName;
        this.comName = comName;
        this.address = address;
        this.dueDate = dueDate;
        this.cusName = cusName;
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
