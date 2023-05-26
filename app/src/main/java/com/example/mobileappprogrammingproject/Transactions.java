package com.example.mobileappprogrammingproject;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

    public Transactions(String transId, int amount, int balanceAfter, int transType) {
        this.transId = transId;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transType = transType;
    }

    public Transactions() {
    }
    public final static int TRANSFER_RECEIVE = 0;
    public final static int TRANSFER_SEND = 1;
    public final static int BANK_DEPOSIT = 2;
    public final static int BANK_WITHDRAW = 3;
    public final static int BILL_INTERNET = 4;
    public final static int BILL_WATER = 5;
    public final static int BILL_ELECTRIC = 6;

    public static int convertBillType(String type){
        int intType = Integer.parseInt(type);
        return new int[]{5, 6, 4}[intType - 1];
    }

    public static String convertBillName(String type){
        int intType = Integer.parseInt(type);
        return new String[]{"Tiền nước tháng", "Tiền điện tháng", "Tiền Internet tháng"}[intType - 1];
    }

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
                R.drawable.withdraw_success_2,
                R.drawable.wifi_icon_2,
                R.drawable.water_icon_2,
                R.drawable.flash_icon_2
        };
    }

    public static int[] getImgSourceDetail(){
        return new int[]{
                R.drawable.transfer_money_icon,
                R.drawable.receive_money_icon,
                R.drawable.wallet_icon,
                R.drawable.withdraw_icon,
                R.drawable.wifi_icon,
                R.drawable.water_icon,
                R.drawable.flash_icon
        };
    }

    public String getTransDetailStr(){
        return
            transType <= 3 ?
            Transactions.getListTransTypeStr()[this.getTransType()] + " " + this.getReceiverOrSender() :
            ((BillTrans) this).getBillName();
    }

    public static Transactions getTransByJSONObj(JSONObject jsonObj, HashMap<String, Integer> bankToFees) throws JSONException {
        //____________________Transaction dang Bill thanh toan_____________________//
        if(GECL.isKeyInJSON(jsonObj, "idDV")){
            String billId = jsonObj.getString("idDV");
            String billType = jsonObj.getString("idLoaiDV");
            String amount = jsonObj.getString("thanhTien");
            String id_SDT = jsonObj.getString("SDT");
            String dueDate = jsonObj.getString("hanDong");
            String payDate = jsonObj.getString("ngayDong");
            String comName = jsonObj.getString("tenCty");
            int balanceAfter = jsonObj.getInt("balanceAfter");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            LocalDateTime payDateTime = payDate.equals("null") ? null : LocalDateTime.parse(payDate, dateFormatter);
            LocalDateTime dueDateTime = LocalDateTime.parse(dueDate, dateFormatter);
            String billName = Transactions.convertBillName(billType) + " " + String.valueOf(dueDateTime.getMonth().getValue() - 1);


            if(payDate.equals("null"))
                return new BillTrans(
                        billId, Integer.parseInt(amount), null, balanceAfter,
                        Transactions.convertBillType(billType), billName, comName,
                        "", dueDateTime, "cusName", id_SDT
                );
            else
                return new BillTrans(
                    billId, Integer.parseInt(amount), payDateTime, balanceAfter,
                    Transactions.convertBillType(billType), billName, comName,
                    "", dueDateTime, "cusName", id_SDT
                );
        }
        //____________________Transaction dang chuyen tien_____________________//
        String transType = jsonObj.getString("LoaiGD");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if("Receive_Send".contains(transType)){

            String loiNhan = jsonObj.getString("message");
            String oppoName = loiNhan.substring(HisTransActivity.indexOfNthChar(loiNhan, ' ', 3) + 1);

            String phoneSender = (String) jsonObj.get("SDT1");
            String phoneReceiver = (String) jsonObj.get("SDT2");
            String phoneNumOppo = transType.equals("Receive") ? phoneSender : phoneReceiver;

            LocalDateTime dateTime = LocalDateTime.parse(jsonObj.getString("NGAYGD"), dateFormatter);
            int fee = jsonObj.getInt("CHIETKHAU");
            int amount = jsonObj.getInt("STGD") + (transType.equals("Send") ? fee : 0);
            String transId = jsonObj.getString("MAGD");
            String message = jsonObj.getString("loiNhan");
            int balanceAfter = jsonObj.getInt("balanceAfter");
            int transTypeId = transType.equals("Receive") ? Transactions.TRANSFER_RECEIVE : Transactions.TRANSFER_SEND;

            return new TransferTrans(transId, amount, fee, dateTime, balanceAfter, new Account(phoneNumOppo, new UserTrans(oppoName)), transTypeId, message);
        }


        //____________________Transaction dang rut va nap_____________________//
        int balanceAfter = jsonObj.getInt("balanceAfter");
        String transId = jsonObj.getString("MAGDNH");
        LocalDateTime dateTime = LocalDateTime.parse(jsonObj.getString("NGAYGD"), dateFormatter);
        String message = jsonObj.getString("message");
        String bankName = message.substring(HisTransActivity.indexOfNthChar(message, ' ', 5));
        int transTypeId = transType.equals("Refill") ? Transactions.BANK_DEPOSIT : Transactions.BANK_WITHDRAW;

        int amount = jsonObj.getInt("SOTIEN");
        int fee = transType.equals("Refill") ? (int)(amount * (1.0 * feePercent(bankName) / 100)) : 0;

        return new BankTrans(transId, amount, dateTime, balanceAfter, new BankAccount(new Bank(bankName)), transTypeId, fee);

    }
    private static int feePercent(String bankName){
        HashMap<String, Integer> bankToFee = new HashMap<>();
        bankToFee.put("Sacombank", 5);
        bankToFee.put("VietTinBank", 6);
        bankToFee.put("Agribank", 4);
        bankToFee.put("TechComBank", 4);

        return bankToFee.get(bankName.strip());
    }

    public Class<? extends AppCompatActivity> getActivityIntentTo(){
        switch (this.transType){
            case TRANSFER_RECEIVE:
                return TransferReceiveTransDetailActivity.class;
            case TRANSFER_SEND:
                return TransferTransDetailActivity.class;
            case BANK_DEPOSIT:
                return DepositTransDetailActivity.class;
            case BANK_WITHDRAW:
                return WithdrawTransDetailActivity.class;
            case BILL_INTERNET:
            case BILL_WATER:
            case BILL_ELECTRIC:
                return PaidBillDetailActivity.class;
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
