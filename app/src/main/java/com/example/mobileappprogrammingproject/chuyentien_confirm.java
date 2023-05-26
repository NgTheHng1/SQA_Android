package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileappprogrammingproject.APIResult.TransferResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chuyentien_confirm extends AppCompatActivity {
    TextView tvTransMoney, tvReceiver, tvPhoneNumber, tvMessage, tvFee, tvTotalMoney;
    Button btnTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuyentien_confirm);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String sotien = bundle.getString("sotien");
        String hoten_ngnhan = bundle.getString("hoten_ngnhan");
        String sdt_ngnhan = bundle.getString("sdt_ngnhan");
        String message = bundle.getString("message");
        String token = GECL.getTokenFromSession(this);

        tvTransMoney = findViewById(R.id.trans_money);
        tvReceiver = findViewById(R.id.receiver);
        tvPhoneNumber = findViewById(R.id.receive_phone_number);
        tvMessage = findViewById(R.id.message);
        tvFee = findViewById(R.id.is_free);
        tvTotalMoney = findViewById(R.id.total_trans_money);
        btnTrans = findViewById(R.id.confirm_trans);

        tvTransMoney.setText(GECL.formatCurrency(sotien, ""));
        tvReceiver.setText(hoten_ngnhan);
        tvPhoneNumber.setText(sdt_ngnhan);
        tvMessage.setText(message);
        tvFee.setText(GECL.formatCurrency(perWithNumStr(sotien, "5"), ""));
        tvTotalMoney.setText(GECL.formatCurrency(perWithNumStr(sotien, "105"), ""));

        btnTrans.setOnClickListener(view -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("STGD", sotien);
                map.put("SDT", sdt_ngnhan);
                map.put("loiNhan", message);

                Call<TransferResult> call  = RetrofitClient.getRetroInterface().executeTransferResult(map);
                call.enqueue(new Callback<TransferResult>() {
                    @Override
                    public void onResponse(Call<TransferResult> call, Response<TransferResult> response) {
                        TransferResult rs = response.body();
                        String code = rs.getCode();
//                        String message = rs.getMessage();
                        UserJSONObject user = rs.getUser();
                        String magd = rs.getMaGD();
                        String sodu = rs.getSodu();
                        if (code.equals("e000")) {
                            Intent intent = new Intent(chuyentien_confirm.this, chuyentien_thanhcong.class);
                            intent.putExtra("sodu", sodu);
                            intent.putExtra("token", token);
                            intent.putExtra("sdt", user.getSdt());//
                            intent.putExtra("hoten", user.getHoten());//
                            intent.putExtra("magd", magd);
                            intent.putExtra("loinhan", message);
                            intent.putExtra("sotien", sotien);
                            intent.putExtra("adminHoten", "Unknown name");
                            intent.putExtra("adminSDT", "Unknown phone number");
                            startActivity(intent);
                            finish();
                        } else {
                            GECL.makeToast(message, chuyentien_confirm.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<TransferResult> call, Throwable t) {
                        Toast.makeText(chuyentien_confirm.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        });

    }
    private String perWithNumStr(String num, String per){
        int base = Integer.parseInt(num);
        int percent = Integer.parseInt(per);
        return String.valueOf(base * percent / 100);
    }
}