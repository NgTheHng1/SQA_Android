package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayBillConfirmActivity extends AppCompatActivity {
    TextView tvComName, tvCusId, tvCusName,
            tvBillName, tvAmount, tvTotalAmount;
    LinearLayout btnPay;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.pay_bill_confirm);

        token = GECL.getTokenFromSession(this);

        setControl();
        renderView();
        setEvent();
    }

    private void renderView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");

        tvComName.setText(bundle.getString("comName"));
        tvCusId.setText(bundle.getString("cusId"));
        tvBillName.setText(bundle.getString("billName"));
        tvAmount.setText(GECL.formatCurrency(bundle.getString("amount"), ""));
        tvTotalAmount.setText(GECL.formatCurrency(bundle.getString("amount"), ""));
        tvCusName.setText(bundle.getString("cusName"));
    }

    private void setControl() {
        tvComName = findViewById(R.id.com_name);
        tvCusId = findViewById(R.id.cus_id);
        tvBillName = findViewById(R.id.bill_name);
        tvAmount = findViewById(R.id.amount);
        tvTotalAmount = findViewById(R.id.total_amount);
        tvCusName = findViewById(R.id.cus_name);
        btnPay = findViewById(R.id.btn_pay);
    }

    private void setEvent() {
        String transId = getIntent().getBundleExtra("bundle").getString("transId");
        for (int i = -1; i < btnPay.getChildCount(); i++) {
            View childView = i != -1 ?  btnPay.getChildAt(i) : btnPay;

            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Promise.resolve()
                            .then((action, data) -> {
                                Call<String> call = RetrofitClient.getRetroInterface().payBill(token, transId);

                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String responseStr = response.body();

                                        try {
                                            JSONObject jsonObj = new JSONObject(responseStr);
                                            jsonObj.getString("status");
                                            String payDate = jsonObj.getString("ngayThanhToan");
                                            String transId = jsonObj.getString("transId");
                                            action.resolve(new String[]{payDate, transId});
                                        } catch (JSONException e) {
                                            GECL.makeToast("Thanh toán thất bại", PayBillConfirmActivity.this);
                                            action.reject();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        GECL.makeToast("Thanh toán thất bại", PayBillConfirmActivity.this);
                                        action.reject();
                                    }
                                });
                            })
                            .then((action, data) -> {
                                String[] getData = (String[]) data;
                                String payDate = getData[0];
                                String transId = getData[1];

                                Intent intent = new Intent(PayBillConfirmActivity.this, PayBillResultActivity.class);
                                Bundle bundle = new Bundle();

                                bundle.putString("billName", tvBillName.getText().toString());
                                bundle.putString("amount", tvAmount.getText().toString());
                                bundle.putString("comName", tvComName.getText().toString());
                                bundle.putString("time", payDate);
                                bundle.putString("transId", transId);

                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            })
                            .start();
                }
            });
        }
    }

}