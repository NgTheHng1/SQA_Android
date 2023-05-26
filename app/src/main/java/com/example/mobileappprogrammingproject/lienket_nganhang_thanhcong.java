package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class lienket_nganhang_thanhcong extends AppCompatActivity {
    Button btnBackHomePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.lienket_nganhang_thanhcong);

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String hoten = GECL.getObjectFromSession(this, "hoten");

        btnBackHomePage = findViewById(R.id.btnBackHomePage);
        btnBackHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(lienket_nganhang_thanhcong.this, HomePageActivity.class);
                intent.putExtra("hoten", hoten);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }
}