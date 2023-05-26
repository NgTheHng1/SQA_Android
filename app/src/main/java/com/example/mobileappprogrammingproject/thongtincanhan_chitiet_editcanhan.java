package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.BasicResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class thongtincanhan_chitiet_editcanhan extends AppCompatActivity {
    Button btnClose, btnCapnhat;
    EditText txtHoten;
    EditText txtCCCD;
    EditText txtGioitinh;
  
     
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.thongtincanhan_chitiet_editcanhan);  btnClose = findViewById(R.id.btnClose);
        btnCapnhat = findViewById(R.id.btnCapnhat);
        txtHoten = findViewById(R.id.txtHoten);
        txtGioitinh = findViewById(R.id.txtGioitinh);
        txtCCCD = findViewById(R.id.txtCCCD);

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String gender = intent.getStringExtra("gioitinh");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String cccd = intent.getStringExtra("cccd");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        Log.e("SDT", sdt);

        txtHoten.setText(hoten);
        if (gender.equals("1")) {
            txtGioitinh.setText("Nữ");
        }
        else {
            txtGioitinh.setText("Nam");
        }
        txtCCCD.setText(cccd);

        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("HoTen", txtHoten.getText().toString());
                map.put("GioiTinh", changeType(txtGioitinh.getText().toString()));
                map.put("CCCD", txtCCCD.getText().toString());

                Call<BasicResult> call  = RetrofitClient.getRetroInterface().excuteUpdateUser(map);
                call.enqueue(new Callback<BasicResult>() {
                    @Override
                    public void onResponse(Call<BasicResult> call, Response<BasicResult> response) {
                        BasicResult rs = response.body();
                        String code = rs.getCode();
                        if(code.equals("e000")) {
                            Intent intent = new Intent(thongtincanhan_chitiet_editcanhan.this, HomePageActivity.class);
                            intent.putExtra("hoten", txtHoten.getText().toString());
                            intent.putExtra("token", token);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            alertDialog(rs.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResult> call, Throwable t) {
                    }
                });
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void alertDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
    }
    public String changeType(String gender) {
        if (gender.equals("Nữ")) {
            return "1";
        }
        else {
            return "0";
        }
    }
}