
package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.print;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileappprogrammingproject.APIResult.GetMoneyResult;
import com.example.mobileappprogrammingproject.APIResult.TransferResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class naptien_confirm extends AppCompatActivity {
    TextView tvTransMoney, tvBankName, tvBankAcc, tvFee, tvTotalMoney, tvTxtFee;
    Button btnTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.naptien_confirm);

        Bundle bundle = getIntent().getBundleExtra("bundle");

        String sotien = bundle.getString("sotien");
        String ten_nghang = bundle.getString("ten_nghang");
        String sotk = bundle.getString("sotk");
        String token = GECL.getTokenFromSession(this);

        tvTransMoney = findViewById(R.id.trans_money);
        tvBankName = findViewById(R.id.bank_withdraw);
        tvBankAcc = findViewById(R.id.bank_account);
        tvFee = findViewById(R.id.is_free);
        tvTotalMoney = findViewById(R.id.total_trans_money);
        btnTrans = findViewById(R.id.confirm_trans);
        tvTxtFee = findViewById(R.id.txt_fee);

        tvBankName.setText(ten_nghang);
        tvBankAcc.setText(sotk);
        tvTransMoney.setText(GECL.formatCurrency(sotien, ""));
//        tvFee.setText(GECL.formatCurrency(perWithNumStr(sotien, "5"), ""));
//        tvTotalMoney.setText(GECL.formatCurrency(perWithNumStr(sotien, "105"), ""));

        Promise.resolve()
            .then((action, data) -> {
                Call<String> call = RetrofitClient.getRetroInterface().getBankFees();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        print(response.body());
                        try {
                            JSONObject jsonObj = new JSONObject(response.body());
                            JSONArray jsonArray = jsonObj.getJSONArray("data");

                            HashMap<String, Integer> bankToFee = new HashMap<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objEle = (JSONObject) jsonArray.get(i);
                                bankToFee.put(objEle.getString("TENNH"), objEle.getInt("CHIETKHAU"));
                            }

                            String totalPercent = String.valueOf(bankToFee.get(ten_nghang) + 100);
                            tvFee.setText(GECL.formatCurrency(perWithNumStr(sotien, String.valueOf(bankToFee.get(ten_nghang))), ""));
                            tvTotalMoney.setText(GECL.formatCurrency(perWithNumStr(sotien, totalPercent), ""));
                            tvTxtFee.setText("Phí giao dịch (" + String.valueOf(bankToFee.get(ten_nghang) + "%):"));
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
            .start();

        btnTrans.setOnClickListener(view -> {
            Promise.resolve()
                .then((action, data) -> {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("token", token);
                    map.put("MATK", sotk);
                    map.put("MONEY", sotien);

                    Call<GetMoneyResult> call  = RetrofitClient.getRetroInterface().executeGetMoneyResult(map);
                    call.enqueue(new Callback<GetMoneyResult>() {
                        @Override
                        public void onResponse(Call<GetMoneyResult> call, Response<GetMoneyResult> response) {
                            GetMoneyResult rs = response.body();
                            String code = rs.getCode();
                            if(code.equals("e000")) {
                                Intent intent = new Intent(naptien_confirm.this, naptien_thanhcong.class);
                                intent.putExtra("sotien", sotien);
                                intent.putExtra("tennh", ten_nghang);
                                intent.putExtra("sotk", sotk);

                                startActivity(intent);
                                finish();
                            }
                            else {
                                GECL.alertDialog(rs.getMessage(), naptien_confirm.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<GetMoneyResult> call, Throwable t) {
                            Toast.makeText(naptien_confirm.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .start();
        });
    }

    private String perWithNumStr(String num, String per){
        int base = Integer.parseInt(num);
        int percent = Integer.parseInt(per);
        return String.valueOf(base * percent / 100);
    }
}