package com.example.mobileappprogrammingproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class naptien_thanhcong extends AppCompatActivity {
    TextView txtSoduvi, txtTransId, txtTime, txtAmount, txtBankName, txtBankAcc;
    Button btnBackHomePage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.naptien_thanhcong);

        txtSoduvi = findViewById(R.id.txtSoduvi);

        txtTransId = findViewById(R.id.trans_id);

        txtTime = findViewById(R.id.time);

        txtAmount = findViewById(R.id.amount);

        txtBankName = findViewById(R.id.bank_name);

        txtBankAcc = findViewById(R.id.bank_account);

        Intent intent = getIntent();
        String Soduvi = GECL.getObjectFromSession(this, "sodu");
        String token = GECL.getObjectFromSession(this, "token");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String ten_nghang = intent.getStringExtra("tennh");
        String sotk = intent.getStringExtra("sotk");

        txtBankName.setText(ten_nghang);
        txtBankAcc.setText(sotk);

        btnBackHomePage = findViewById(R.id.btnBackHomePage);

        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.successful_gif);
            ((GifImageView)findViewById(R.id.check_logo)).setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        btnBackHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(naptien_thanhcong.this, HomePageActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("sdt", sdt);
                intent.putExtra("hoten", hoten);
                startActivity(intent);
                finish();
            }
        });
        Promise.resolve()
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                int balance = jsonObj.getJSONObject("balance").getInt("SoDu");
                                txtSoduvi.setText(GECL.formatCurrency(balance, ""));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", naptien_thanhcong.this);
                            action.reject();
                        }
                    });
                })
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getLatestTransInfo(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                LocalDateTime datetime = LocalDateTime.parse(
                                        jsonObj.getString("time"),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                );
                                String time = datetime.format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"));
                                txtTime.setText(time);
                                txtTransId.setText(jsonObj.getString("transId"));
                                txtAmount.setText(GECL.formatCurrency(jsonObj.getString("amount"), ""));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", naptien_thanhcong.this);
                            action.reject();
                        }
                    });
                })
            .start();
    }
}