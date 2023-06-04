package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Action;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    HisTransActivity extends AppCompatActivity {;
    ImageButton btnBack;
    Button btnAllTrans, btnDepositTrans, btnWithTrans, btnReceiveTrans, btnTransferTrans, btnPaidBills;
    ImageButton imgBtnClose;
    LinearLayout homePageAct, userInfoAct;
    TextView titleLayout;
    EditText edtTransSearch;
    RecyclerView recyclerView;
    List<List<Transactions>> listTrans;
    int curBalance;
    String token;
    RetrofitClient.RetrofitInterface apiService;
    HashMap<String, Integer> bankToFeePer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.history_transaction);

        apiService = RetrofitClient.getRetroInterface();
        listTrans = new ArrayList<>();
        token = GECL.getTokenFromSession(this);
        bankToFeePer = new HashMap<>();

        setControl();
        setEvent();
        getData_RenderView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(HisTransActivity.this, HomePageActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(HisTransActivity.this, R.anim.slide_in_left, R.anim.slide_out_right);
        ActivityCompat.startActivity(HisTransActivity.this, intent, options.toBundle());
    }

    private void getData_RenderView(){
        Func rejectFunc = (action, data) -> {
            action.reject();
        };
        Func getTokenAuthFunc = (action, data) -> {
            Call<String> call = apiService.getAuthToken("0985758333", "345454");

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response){
                    try {
                        String responseStr = response.body();
                        JSONObject jsonObj = new JSONObject(responseStr);
                        token = (String) jsonObj.get("token");

                        action.resolve();
                    } catch (JSONException e) {
                        action.reject();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("ResBody", t.getMessage());
                    action.reject();
                }
            });
        };
        Func getPaidBillListFunc = (action, data) -> {
            print("messagemessage");
            Call<String> call = apiService.getAllBill(token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    action.resolve(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    action.reject();
                    print(t.getMessage());
                }
            });
        };
        Func getTransListFunc = (action, data) -> {
            String responseBillList = (String) data;

            Call<String> call = apiService.getAllTrans(token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //Convert response string to list of JSONObject and make it to tmp trans list
                    String responseStr = response.body();
                    ArrayList<JSONObject> jsonObjList = getFilteredJSONObject(responseStr, responseBillList, action);
                    ArrayList<Transactions> tmpListTrans = new ArrayList<>();

                    for (JSONObject jsonObj : jsonObjList) {
                        try {
                            tmpListTrans.add(Transactions.getTransByJSONObj(jsonObj, bankToFeePer));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }



                    for(Transactions tmpTrans : tmpListTrans){
                        boolean isAdded = false;
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/yyyy");
                        String tmpDateStr = tmpTrans.getTime().format(dateFormat);
                        for (int i = 0; i < listTrans.size(); i++) {
                            if(listTrans.get(i).get(0).getTime().format(dateFormat).equals(tmpDateStr)){
                                listTrans.get(listTrans.size() - 1).add(tmpTrans);
                                isAdded = true;
                            }
                        }
                        if(!isAdded){
                            listTrans.add(new ArrayList<>());
                            listTrans.get(listTrans.size() - 1).add(tmpTrans);
                        }
                    }

                    action.resolve();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    action.reject();
                }
            });
        };
        Func renderView = (action, data) -> {
            recyclerView = findViewById(R.id.recy_total_trans_his);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listTrans));

        };

        Promise.resolve()
                //get balance of user
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                curBalance = jsonObj.getJSONObject("balance").getInt("SoDu");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", HisTransActivity.this);
                            action.reject();
                        }
                    });
                })
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getBankFees();
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            print(response.body());
                            try {
                                JSONObject jsonObj = new JSONObject(response.body());
                                JSONArray jsonArray = jsonObj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objEle = (JSONObject) jsonArray.get(i);
                                    bankToFeePer.put(objEle.getString("TENNH"), objEle.getInt("CHIETKHAU"));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            print(t.getMessage());
                            action.reject();
                        }
                    });
                })
                .then(getPaidBillListFunc, rejectFunc)
                .then(getTransListFunc, rejectFunc)
                .then(renderView)
                .start();
        print("");
    }

    private void setControl() {
        btnAllTrans = findViewById(R.id.btn_trans_all);
        btnDepositTrans = findViewById(R.id.btn_trans_deposit);
        btnWithTrans = findViewById(R.id.btn_trans_withdraw);
        btnReceiveTrans = findViewById(R.id.btn_trans_receive);
        btnTransferTrans = findViewById(R.id.btn_trans_transfer);
        titleLayout = findViewById(R.id.title_layout);
        edtTransSearch = findViewById(R.id.trans_search);
        homePageAct = findViewById(R.id.home_page_btn);
        userInfoAct = findViewById(R.id.user_info_btn);
        btnPaidBills = findViewById(R.id.btn_paid_bill);
        imgBtnClose = findViewById(R.id.close_icon);
    }

    private void setEvent() {
//        btnBack.setOnClickListener(view -> onBackPressed());
        setEventButtons(btnAllTrans, -1, true);
        setEventButtons(btnReceiveTrans, 0, false);
        setEventButtons(btnTransferTrans, 1, false);
        setEventButtons(btnDepositTrans, 2, false);
        setEventButtons(btnWithTrans, 3, false);
        setEventButtons(btnPaidBills, -1, false);


        edtTransSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                GECL.hideKeyboard(HisTransActivity.this);
                filterTransByText(edtTransSearch.getText().toString().strip());
                edtTransSearch.clearFocus();
                return true;
            }
            return false;
        });
        edtTransSearch.setOnFocusChangeListener((view, b) -> {
            if(b){
                edtTransSearch.setBackground(ContextCompat.getDrawable(HisTransActivity.this, R.drawable.history_input_search_focus));
            }else{
                edtTransSearch.setBackground(ContextCompat.getDrawable(HisTransActivity.this, R.drawable.history_input_search));
            }
        });

        //Home page activity
        for (int i = -1; i < homePageAct.getChildCount(); i++) {
            View view = i != -1 ?  homePageAct.getChildAt(i) : homePageAct;
            view.setOnClickListener(view4 -> {
                onBackPressed();
            });
        }

        //User info activity
        for (int i = -1; i < userInfoAct.getChildCount(); i++) {
            View view = i != -1 ?  userInfoAct.getChildAt(i) : userInfoAct;
            view.setOnClickListener(view4 -> {
                Intent intent = new Intent(HisTransActivity.this, thongtincanhan_main.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(HisTransActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCompat.startActivity(HisTransActivity.this, intent, options.toBundle());
            });
        }
        GECL.setCloseBtnEvent(this, imgBtnClose);
    }

    private void setEventButtons(Button btn, int transType, boolean all){
        btn.setOnClickListener(view -> {
            edtTransSearch.clearFocus();
            GECL.hideKeyboard(HisTransActivity.this);
            highlightButton(btn);
            if(all)
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listTrans));
            else
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, filteredTransListByType(transType)));
        });
    }

    private List<List<Transactions>> filteredTransListByType(int transType){
        List<List<Transactions>> listResult = new ArrayList<>();
        for (List<Transactions> tmpCheckingList : listTrans) {
            List<Transactions> tmpFilteredList = new ArrayList<>();

            for (Transactions transaction : tmpCheckingList) {

                if(transaction.getTransType() == transType){
                    tmpFilteredList.add(transaction);
                    continue;
                }
                if(
                    transaction.getTransType() >= 4 &&
                    transaction.getTransType() <= 6 &&
                    transType == -1
                ){
                    tmpFilteredList.add(transaction);
                }
            }
            if(tmpFilteredList.size() != 0)
                listResult.add(tmpFilteredList);
        }
        if(listResult.size() == 0) {
            Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
        return listResult;
    }
    void highlightButton(Button btn){
        btn.setBackgroundResource(R.drawable.clicked_button);
        btn.setTextColor(ContextCompat.getColor(HisTransActivity.this, R.color.app_color));

        int[] listIdButton = new int[]{
                R.id.btn_trans_all,
                R.id.btn_trans_receive,
                R.id.btn_trans_transfer,
                R.id.btn_trans_deposit,
                R.id.btn_trans_withdraw,
                R.id.btn_paid_bill
        };
        for(int btnId : listIdButton){
            if(btnId == btn.getId())
                continue;
            Button btnTmp = findViewById(btnId);
            btnTmp.setBackgroundResource(R.drawable.unclicked_button);
            btnTmp.setTextColor(ContextCompat.getColor(HisTransActivity.this, R.color.black));
        }
    }

    private void filterTransByText(String rawSearch){
        if(rawSearch.equals(""))
            return;
        String search = GECL.toUnaccentedString(rawSearch);
        List<List<Transactions>> resultList = new ArrayList<>();

        for (List<Transactions> tmpScanList: listTrans){
            List<Transactions> tmpHoldList = new ArrayList<>();

            for (Transactions trans : tmpScanList){
                String strToCheck = trans.getTransDetailStr();
                if(GECL.toUnaccentedString(strToCheck).contains(search)){
                    tmpHoldList.add(trans);
                }
            }
            if(tmpHoldList.size() != 0)
                resultList.add(tmpHoldList);
        }
        if(resultList.size() == 0){
            Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerView.setAdapter(new TotalTransViewAdapter(this, resultList));
    }

    ArrayList<JSONObject> getFilteredJSONObject(String responseTransStr, String responseBillStr, Action action) { //sort lai va them afterBalance
        ArrayList<JSONObject> arrayJSONReturn = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(responseTransStr);
            JSONArray jsonArrayTrans = new JSONObject(responseTransStr).getJSONArray("data");
            JSONArray jsonArrayBills = new JSONObject(responseBillStr).getJSONArray("data");

            //Thêm phần tử của của jsonArray từ 2 response string billTrans và Trans
            for (int i = 0; i < jsonArrayTrans.length(); i++) {
                arrayJSONReturn.add(jsonArrayTrans.getJSONObject(i));
            }
            for (int i = 0; i < jsonArrayBills.length(); i++) {
                if(!jsonArrayBills.getJSONObject(i).getString("ngayDong").equals("null"))
                    arrayJSONReturn.add(jsonArrayBills.getJSONObject(i));
            }

            //Sort Array of JsonObject by its dateTime
            arrayJSONReturn.sort((json1, json2) -> {
                LocalDateTime time1 = null;
                LocalDateTime time2 = null;

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    String payDateKeyJSON1 = GECL.isKeyInJSON(json1, "ngayDong") ? "ngayDong" : "NGAYGD";
                    String payDateKeyJSON2 = GECL.isKeyInJSON(json2, "ngayDong") ? "ngayDong" : "NGAYGD";

                    time1 = LocalDateTime.parse((String) json1.get(payDateKeyJSON1), formatter);
                    time2 = LocalDateTime.parse((String) json2.get(payDateKeyJSON2), formatter);
                } catch (JSONException e) {
                    GECL.makeToast("Lỗi", HisTransActivity.this);
                }
                return time2.compareTo(time1);
            });

            //Add every transaction with its balance after transaction
            int tmpBalance = curBalance;
            for(JSONObject jsonObjEle : arrayJSONReturn){
                boolean isThisTransBill = GECL.isKeyInJSON(jsonObjEle, "ngayDong");
                String transType, balanceStrInJSON;
                int sign;
                double fee = 1;

                if(!isThisTransBill){
                    transType = jsonObjEle.getString("LoaiGD");
                    sign = "Receive_Refill".contains(transType) ? -1 : 1;
                    balanceStrInJSON = "Receive_Send".contains(transType) ? "STGD" : "SOTIEN";
                    fee = transType.equals("Send") ? 1.05 : 1;
                }else{
                    sign = 1;
                    balanceStrInJSON = "thanhTien";
                }

                jsonObjEle.put("balanceAfter", tmpBalance);

                //Luu balance hien tai cho balanceAfter cua transaction moi nhat
                tmpBalance += sign * jsonObjEle.getInt(balanceStrInJSON) * fee;
            }
        } catch (JSONException e) {
            action.reject();
            throw new RuntimeException(e);
        }
        return arrayJSONReturn;
    }

    public static int indexOfNthChar(String whole, char find, int nth){
        int index = whole.indexOf(find);
        while (--nth > 0 && index != -1) {
            index = whole.indexOf(find, index + 1);
        }
        return index;
    }
}
//Filter trans list by month and year into ArrayList<ArrayList<Transactions>>
//                    String preMonthYearStr = "";
//                    for(Transactions transEle : tmpListTrans){
//                        String monthYearStr = transEle.getTime().format(DateTimeFormatter.ofPattern("MM/yyyy"));
//                        if(monthYearStr.equals(preMonthYearStr)){
//                            listTrans.get(listTrans.size() - 1).add(transEle);
//                        }else{
//                            ArrayList<Transactions> listToAdd = new ArrayList<>();
//                            listToAdd.add(transEle);
//                            listTrans.add(listToAdd);
//                            preMonthYearStr = monthYearStr;
//                        }
//                    }