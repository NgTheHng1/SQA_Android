package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ConfirmWithdrawActivity extends AppCompatActivity {
    boolean isActivityPaused = false;
    Button btnConfrmTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_confirm);
        setControl();
        setEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityPaused){
            isActivityPaused = false;
            onBackPressed();
        }
    }

    private void setControl(){
        btnConfrmTrans = findViewById(R.id.confirm_trans);
    }

    private void setEvent(){
        setBtnConfrmTransEvent();
    }

    private void setBtnConfrmTransEvent() {
        btnConfrmTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePage = new Intent(ConfirmWithdrawActivity.this, TransResultActivity.class);
                startActivity(movePage);
            }
        });
    }
}