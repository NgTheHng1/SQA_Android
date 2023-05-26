package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.LoginResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class dangNhap extends AppCompatActivity {
    Button btnDangnhap, btnSetVisibility;
    Button btnDangky;
    Button btnQuenmatkhau;
    EditText txtSdt;
    EditText txtPassword;
  
     
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.dangnhap);

         


        txtSdt = findViewById(R.id.txtsdt);
        txtPassword = findViewById(R.id.txtpassword);
        btnDangnhap = findViewById(R.id.btnDangnhap);
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();

            }
        });

        btnSetVisibility = findViewById(R.id.btnSetVisibility);
        btnSetVisibility.setOnClickListener(new View.OnClickListener() {
            boolean check = false;
            @Override
            public void onClick(View view) {
                if(check == false) {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnSetVisibility.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off, 0, 0, 0);
                    check = true;
                }
                else {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnSetVisibility.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility, 0, 0, 0);
                    check = false;
                }
            }
        });

        btnDangky = findViewById(R.id.btnDangky);
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangNhap.this, dangky_main.class);
                startActivity(intent);
                finish();
            }
        });

        btnQuenmatkhau = findViewById(R.id.btnQuenmatkhau);
        btnQuenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangNhap.this, quenmatkhau_nhapemail.class);
                startActivity(intent);
            }
        });
    }
    public void handleLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("SDT", txtSdt.getText().toString());
        map.put("Password", txtPassword.getText().toString());

        Call<LoginResult> call  = RetrofitClient.getRetroInterface().executeLogin(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult rs = response.body();
                String code = rs.getCode();
                Boolean success = rs.getSuccess();
                if(success == true && code.equals("e000")) {
                    Toast.makeText(dangNhap.this, rs.getMessage(), Toast.LENGTH_SHORT).show();
                    String token = rs.getToken();
                    UserJSONObject obj = rs.getUser();
                    Intent intent = new Intent(dangNhap.this, HomePageActivity.class);
                    intent.putExtra("hoten", obj.getHoten());
                    intent.putExtra("sdt", obj.getSdt());
                    intent.putExtra("sodu", obj.getSodu());
                    intent.putExtra("token", token);
                    Log.e("Error", token);
                    startActivity(intent);
                    finish();
                }
                else {
                    alertDialog(rs.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(dangNhap.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}