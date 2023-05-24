package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class PayBillResultActivity extends AppCompatActivity {
    TextView tvBillName, tvAmount, tvForCompany, 
            tvBillName2, tvTime, tvTransId;
    GifImageView gifImageView;
    Button btnHomePage, btnNewPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.pay_bill_result);
        
        setControl();
        renderView();
        setEvent();
    }

    private void setEvent() {
        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(PayBillResultActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        btnNewPay.setOnClickListener(view -> {
            onBackPressed();
//            Intent intent = new Intent(PayBillResultActivity.this, PayBillViewActivity.class);
//            startActivity(intent);
        });
    }

    private void renderView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");

        tvBillName.setText(bundle.getString("billName"));
        tvBillName2.setText(bundle.getString("billName"));
        tvAmount.setText(bundle.getString("amount"));
        tvForCompany.setText("cho " + bundle.getString("comName"));
        tvTime.setText(bundle.getString("time"));
        tvTransId.setText(bundle.getString("transId"));

        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.successful_gif);
            gifImageView.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setControl() {
        tvBillName = findViewById(R.id.bill_name);
        tvBillName2 = findViewById(R.id.bill_name_2);
        tvAmount = findViewById(R.id.amount);
        tvForCompany = findViewById(R.id.for_company);
        tvTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        gifImageView = findViewById(R.id.check_logo);
        btnHomePage = findViewById(R.id.back_homepage);
        btnNewPay = findViewById(R.id.new_pay);
    }
}