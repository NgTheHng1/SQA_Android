package com.example.mobileappprogrammingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayBillViewActivity extends AppCompatActivity {
    TextView tvBalance, tvConDeposit;
    Button btnYetPaid, btnPaid;
    List<BillTrans> listPaidBill, listBill;
    RecyclerView recyclerView;
    LinearLayout forEmpty;
    boolean isActivityPaused = false;
    String token;
    RetrofitClient.RetrofitInterface apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.pay_bill);

        listBill = new ArrayList<>();
        listPaidBill = new ArrayList<>();
        apiService = RetrofitClient.getRetroInterface();
        token = GECL.getTokenFromSession(this);

        setControl();
        setEvent();
        getData_RenderView();
    }

    private void getData_RenderView() {
        final String[] cusName = new String[1];
        listBill = new ArrayList<>();
        listPaidBill = new ArrayList<>();

        Promise.resolve()
                .then((action, data) -> {
                    Call<String> call = apiService.getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                String balance = jsonObj.getJSONObject("balance").getString("SoDu");
                                tvBalance.setText(GECL.formatCurrency(balance, ""));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", PayBillViewActivity.this);
                            action.reject();
                        }
                    });
                })
                //get user real name
                .then((action, data) -> {
                    Call<String> call = apiService.getUserName(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr).getJSONObject("user");
                                cusName[0] = jsonObj.getString("HoTen");

                                action.resolve(cusName[0]);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.print(t.getMessage());
                        }
                    });
                })
                //get all bill
                .then((action, data) -> {
                    String getData = (String) data;
                    Call<String> call = apiService.getAllBill(token);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                String responseStr = response.body();
                                JSONArray jsonArray = new JSONObject(responseStr).getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                                    String billId = jsonObj.getString("idDV");
                                    String billType = jsonObj.getString("idLoaiDV");
                                    String amount = jsonObj.getString("thanhTien");
                                    String id_SDT = jsonObj.getString("SDT");
                                    String dueDate = jsonObj.getString("hanDong");
                                    String payDate = jsonObj.getString("ngayDong");
                                    String comName = jsonObj.getString("tenCty");
                                    String cusName = getData;

                                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    LocalDateTime payDateTime = payDate == "null" ? null : LocalDateTime.parse(payDate, dateFormatter);
                                    LocalDateTime dueDateTime = LocalDateTime.parse(dueDate, dateFormatter);
                                    String billName = Transactions.convertBillName(billType) + " " + String.valueOf(dueDateTime.getMonth().getValue() - 1);


                                    if(payDate.equals("null")){
                                        BillTrans bill = new BillTrans(
                                                billId, Integer.parseInt(amount), null, 0,
                                                Transactions.convertBillType(billType), billName, comName,
                                                "", dueDateTime, cusName, id_SDT
                                        );
                                        listBill.add(bill);
                                    }
                                    else{
                                        BillTrans bill = new BillTrans(
                                                billId, Integer.parseInt(amount), payDateTime, 0,
                                                Transactions.convertBillType(billType), billName, comName,
                                                "", dueDateTime, cusName, id_SDT
                                        );
                                        listPaidBill.add(bill);
                                    }
                                }
                                action.resolve();
                            } catch (JSONException e) {
                                action.reject();
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            action.reject();
                        }
                    });
                })
                .then((action, data) -> {
                    usingData_Reload();
                })//render view
                .start();
    }

    void usingData_Reload(){
//        if(listBill.size() == 0){
//            findViewById(R.id.scroll_view).setVisibility(View.GONE);
//            return;
//        }
        forEmpty.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BillViewAdapter(PayBillViewActivity.this, listBill));
    }

    private void setEvent() {
        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPaid.setBackground(ContextCompat.getDrawable(PayBillViewActivity.this, R.drawable.right_pay_button_clicked));
                btnPaid.setTextColor(ContextCompat.getColor(PayBillViewActivity.this, R.color.white));

                btnYetPaid.setBackground(null);
                btnYetPaid.setTextColor(ContextCompat.getColor(PayBillViewActivity.this, R.color.app_color));

                recyclerView.setAdapter(new BillPaidViewAdapter(PayBillViewActivity.this, listPaidBill));
            }
        });

        btnYetPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnYetPaid.setBackground(ContextCompat.getDrawable(PayBillViewActivity.this, R.drawable.left_pay_button_clicked));
                btnYetPaid.setTextColor(ContextCompat.getColor(PayBillViewActivity.this, R.color.white));

                btnPaid.setBackground(null);
                btnPaid.setTextColor(ContextCompat.getColor(PayBillViewActivity.this, R.color.app_color));

                recyclerView.setAdapter(new BillViewAdapter(PayBillViewActivity.this, listBill));
            }
        });

        tvConDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PayBillViewActivity.this, null);
//                startActivity(intent);
            }
        });

    }

    private void setControl() {
        tvBalance = findViewById(R.id.balance);
        tvConDeposit = findViewById(R.id.continue_deposit);
        btnYetPaid = findViewById(R.id.btn_yet_paid);
        btnPaid = findViewById(R.id.btn_paid);
        recyclerView = findViewById(R.id.recycler_view);
        forEmpty = findViewById(R.id.text_for_empty);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityPaused){
            if (btnYetPaid.getBackground() != null) {
                getData_RenderView();
            }
            isActivityPaused = false;
        }
    }
    public class Item {
        private int idDV;
        private int idLoaiDV;
        private double thanhTien;
        private String SDT;
        private String ngayNhap;
        private String hanDong;
        private int trangThai;
        private String ghiChu;
        private String ngayDong;
        private String tenLoaiDV;
        private String tenCty;

        // Các phương thức getter và setter
    }
}
