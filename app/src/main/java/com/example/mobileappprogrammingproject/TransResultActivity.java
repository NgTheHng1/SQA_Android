package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TransResultActivity extends AppCompatActivity {
    Button btnBackHome, btnNewWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_result);
        setControl();
        setEvent();
    }

    private void setControl() {
        btnBackHome = findViewById(R.id.back_homepage);
        btnNewWithdraw = findViewById(R.id.new_withdraw);

    }

    private void setEvent() {
        setBtnNewWithdraw();
        setBtnBackHomeEvent();
    }

    private void setBtnBackHomeEvent() {
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePage = new Intent(TransResultActivity.this, MainActivity.class);
                startActivity(movePage);
            }
        });
    }

    private void setBtnNewWithdraw() {
        btnNewWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePage = new Intent(TransResultActivity.this, WithdrawActiveActivity.class);
                startActivity(movePage);
            }
        });
    }
}