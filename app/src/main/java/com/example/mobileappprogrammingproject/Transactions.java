package com.example.mobileappprogrammingproject;

import java.time.LocalDateTime;

public class Transactions {
    private String transId;
    private int amount;
    private LocalDateTime time;
    private int balanceAfter;
    private int transType;

    public Transactions(String transId, int amount, LocalDateTime time, int balanceAfter, int transType) {
        this.transId = transId;
        this.amount = amount;
        this.time = time;
        this.balanceAfter = balanceAfter;
        this.transType = transType;
    }

    public Transactions() {
    }
    public final static int TRANSFER_RECEIVE = 0;
    public final static int TRANSFER_SEND = 1;
    public final static int BANK_DEPOSIT = 2;
    public final static int BANK_WITHDRAW = 3;

    public static String[] getListTransTypeStr(){
        return new String[]{
                "Nhận tiền từ",
                "Chuyển tiền đến",
                "Nạp tiền vào ví từ",
                "Rút tiền từ ví về"
        };
    }

    public static String[] getListSimpleTransTypeStr(){
        return new String[]{
                "Nhận tiền",
                "Chuyển tiền",
                "Nạp tiền",
                "Rút tiền"
        };
    }

    public static int[] getImgSource(){
        return new int[]{
                R.drawable.receive_money_success_2,
                R.drawable.transfer_success_2,
                R.drawable.deposit_success_2,
                R.drawable.withdraw_success_2
        };
    }

    public static int[] getImgSourceDetail(){
        return new int[]{
                R.drawable.transfer_money_icon,
                R.drawable.receive_money_icon,
                R.drawable.wallet_icon,
                R.drawable.withdraw_icon
        };
    }

    public Class getActivityIntentTo(){
        switch (this.transType){
            case TRANSFER_RECEIVE:
                return TransferReceiveTransDetailActivity.class;
            case TRANSFER_SEND:
                return TransferTransDetailActivity.class;
            case BANK_DEPOSIT:
                return DepositTransDetailActivity.class;
            case BANK_WITHDRAW:
                return WithdrawTransDetailActivity.class;
            default:
                return null;
        }
    }
    public String getPrefixOfAmount(){
        return transType % 2 == 0 ? "+" : "-";
    }
    public String getReceiverOrSender(){
        return transType <= 1
                ? ((TransferTrans)this).getOppoPerson().getUser().getFullName()
                : ((BankTrans)this).getBankAccount().getBank().getBankName();
    }


    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
