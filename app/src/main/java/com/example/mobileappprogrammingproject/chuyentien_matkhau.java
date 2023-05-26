package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chuyentien_matkhau extends AppCompatActivity {
  
     
      
    TextView tenNguoiNhan, sdtNguoiNhan;
    EditText txtpassword;
    Button btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.chuyentien_matkhau);
         

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String userHoten = intent.getStringExtra("userHoten");
        String userSdt = intent.getStringExtra("userSdt");
        tenNguoiNhan = findViewById(R.id.tenNguoiNhan);
        tenNguoiNhan.setText(userHoten);
        sdtNguoiNhan = findViewById(R.id.sdtNguoiNhan);
        sdtNguoiNhan.setText(userSdt);

        btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}