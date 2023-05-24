package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.formatCurrency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmWithdrawActivity extends AppCompatActivity {
    boolean isActivityPaused = false;
    Button btnConfrmTrans;
    TextView bankWithdraw, moneyWithdraw, totalMoney;
    String token;
    String bankAcc, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.trans_confirm);
        setControl();
        getData();
        setEvent();
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int amount = bundle.getInt("amount");
        String bankAcc = bundle.getString("bankAcc");
        String bankName = bundle.getString("bankName");
        token = GECL.getTokenFromSession(this);

        moneyWithdraw.setText(formatCurrency(amount, ""));
        totalMoney.setText(formatCurrency(amount, ""));
        bankWithdraw.setText(bankName);

        this.bankAcc = bankAcc;
        this.amount = String.valueOf(amount);

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
            isActivityPaused = false;
            onBackPressed();
        }
    }

    private void setControl(){
        btnConfrmTrans = findViewById(R.id.confirm_trans);
        bankWithdraw = findViewById(R.id.bank_withdraw);
        moneyWithdraw = findViewById(R.id.trans_money);
        totalMoney = findViewById(R.id.total_trans_money);
    }

    private void setEvent(){
        setBtnConfrmTransEvent();
    }

    private void setBtnConfrmTransEvent() {
        final String[] time = new String[1];
        final String[] transId = new String[1];
        String amount = this.amount;
        String bankAcc = this.bankAcc;

        btnConfrmTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.RetrofitInterface apiService;
                apiService = RetrofitClient.getRetroInterface();

                Promise.resolve()
                        .then((action, data) -> {
                            Call<String> call = apiService.withdrawMoneyTrans(token, bankAcc, amount);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    try {
                                        String reponseStr = response.body();
                                        JSONObject jsonObj = new JSONObject(reponseStr);

                                        if(jsonObj.getString("code").equals("e000")) {
                                            time[0] = jsonObj.getString("time");
                                            transId[0] = jsonObj.getString("MaGiaoDich");
                                            action.resolve();
                                        }else
                                            action.reject();
                                    } catch (JSONException e) {
                                        Log.e("Error", e.getMessage());
                                        action.reject();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e("Error", t.getMessage());
                                    action.reject();
                                }
                            });
                        })
                        .then((action, data) -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("amount", amount);
                            bundle.putString("time", time[0]);
                            bundle.putString("transId", transId[0]);

                            Intent movePage = new Intent(ConfirmWithdrawActivity.this, TransResultActivity.class);
                            movePage.putExtra("bundle", bundle);
                            startActivity(movePage);
                        }, ((action, data) -> {
                            Toast.makeText(ConfirmWithdrawActivity.this, "Giao dịch bị lỗi", Toast.LENGTH_SHORT).show();
                        }))
                        .start();
            }
        });
    }
}