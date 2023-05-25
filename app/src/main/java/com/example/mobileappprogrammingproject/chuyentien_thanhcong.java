package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chuyentien_thanhcong extends AppCompatActivity {
    TextView txtSoduvi, txtSotien, txtMaGD, txtHoten, txtSdt, txtLoinhan;
    Button btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuyentien_thanhcong);

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        String sodu = GECL.getObjectFromSession(this, "sodu");
        String magd = intent.getStringExtra("magd");
        String loinhan = intent.getStringExtra("loinhan");
        String sotien = intent.getStringExtra("sotien");
        String adminHoten = intent.getStringExtra("adminHoten");
        String adminSDT = intent.getStringExtra("adminSDT");

        txtSoduvi = findViewById(R.id.txtSoduvi);
        txtSoduvi.setText(sodu);
        txtSotien = findViewById(R.id.txtSotien);
        txtSotien.setText(sotien);
        txtMaGD = findViewById(R.id.txtMaGD);
        txtMaGD.setText(magd);
        txtHoten = findViewById(R.id.txtHoten);
        txtHoten.setText(hoten);
        txtSdt = findViewById(R.id.txtSdt);
        txtSdt.setText(sdt);
        txtLoinhan = findViewById(R.id.txtLoinhan);
        txtLoinhan.setText(loinhan);

        btnAccept =findViewById(R.id.btnAccept);
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
                .start();
    }
}