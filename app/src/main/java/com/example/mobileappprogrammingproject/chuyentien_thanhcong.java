package com.example.mobileappprogrammingproject;

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

public class chuyentien_thanhcong extends AppCompatActivity {
    TextView txtSoduvi, txtSotien, txtMaGD, txtHoten, txtSdt, txtLoinhan, txtTime;
    Button btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.chuyentien_thanhcong);

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String hoten = intent.getStringExtra("hoten");
        String sdt = intent.getStringExtra("sdt");
        String sodu = GECL.getObjectFromSession(this, "sodu");
        String magd = intent.getStringExtra("magd");
        String loinhan = intent.getStringExtra("loinhan");
        String sotien = intent.getStringExtra("sotien");
        String adminHoten = intent.getStringExtra("adminHoten");
        String adminSDT = intent.getStringExtra("adminSDT");

        txtSoduvi = findViewById(R.id.txtSoduvi);
        txtSoduvi.setText(sodu);
        txtSotien = findViewById(R.id.txtSotien);
        txtSotien.setText(GECL.formatCurrency(sotien, ""));
        txtMaGD = findViewById(R.id.txtMaGD);
        txtMaGD.setText(magd);
        txtHoten = findViewById(R.id.txtHoten);
        txtHoten.setText(hoten);
        txtSdt = findViewById(R.id.txtSdt);
        txtSdt.setText(sdt);
        txtLoinhan = findViewById(R.id.txtLoinhan);
        txtLoinhan.setText(loinhan);
        txtTime = findViewById(R.id.time);
        btnAccept =findViewById(R.id.btnAccept);

        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.successful_gif);
            ((GifImageView)findViewById(R.id.check_logo)).setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chuyentien_thanhcong.this, HomePageActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("hoten", adminHoten);
                intent.putExtra("sdt", adminSDT);
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
                            GECL.makeToast("Lỗi", chuyentien_thanhcong.this);
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
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", chuyentien_thanhcong.this);
                            action.reject();
                        }
                    });
                })
                .start();
    }
}