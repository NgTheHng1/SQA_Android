package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DepositTransDetailActivity extends AppCompatActivity {
    TextView tvTransType, tvAmount, tvTransTime,
            tvTransId, tvSender, tvBalanceAfter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_trans_detail);
        getData();
        setControl();
        setEvent();
    }

    private void getData() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundlePackage");
    }

    private void setControl() {
        tvTransType = findViewById(R.id.text_transfer);
        tvAmount = findViewById(R.id.amount_money);
        tvTransTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        tvSender = findViewById(R.id.wallet_card);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);

        tvTransType.setText(bundle.getString("transType"));
        tvAmount.setText(bundle.getString("amount"));
        tvTransTime.setText(bundle.getString("transTime"));
        tvTransId.setText(bundle.getString("transId"));
        tvSender.setText(bundle.getString("sender"));
        tvBalanceAfter.setText(bundle.getString("balanceAfter"));
    }

    private void setEvent() {
    }

}