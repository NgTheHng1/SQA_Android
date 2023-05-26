package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.ForgetpasswordResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class quenmatkhau_nhapemail extends AppCompatActivity {
    Button btnAccept, btnClose;
    EditText txtEmail;
  
     
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.quenmatkhau_nhapemail);
        btnAccept = findViewById(R.id.btnAccept);
        btnClose = findViewById(R.id.btnClose);
        txtEmail = findViewById(R.id.txtEmail);  btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("mail", txtEmail.getText().toString());

                Call<ForgetpasswordResult> call  = RetrofitClient.getRetroInterface().executeForgetpassword(map);
                call.enqueue(new Callback<ForgetpasswordResult>() {
                    @Override
                    public void onResponse(Call<ForgetpasswordResult> call, Response<ForgetpasswordResult> response) {
                        ForgetpasswordResult rs = response.body();
                        String code = rs.getCode();
                        String message = rs.getMessage();
                        if(code.equals("e000")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(quenmatkhau_nhapemail.this);
                            builder.setMessage("Mật khẩu mới đã gửi đến email của bạn. Vui lòng kiểm tra.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    Intent intent = new Intent(quenmatkhau_nhapemail.this, dangNhap.class);
                                    startActivity(intent);
                                    finish();
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
                        else {
                            alertDialog(message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgetpasswordResult> call, Throwable t) {
                        Toast.makeText(quenmatkhau_nhapemail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}