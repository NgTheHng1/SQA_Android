package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DepositTransDetailActivity extends AppCompatActivity {
    TextView tvTransType, tvAmount, tvTransTime,
            tvTransId, tvSender, tvBalanceAfter,
            tvFee, tvAmountLessBank;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.deposit_trans_detail);
        getData();
        setControl();
        setEvent();
    }

    private void getData() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
    }

    private void setControl() {
        tvTransType = findViewById(R.id.text_transfer);
        tvAmount = findViewById(R.id.amount_money);
        tvTransTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        tvSender = findViewById(R.id.wallet_card);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);
        tvFee = findViewById(R.id.trans_fee);
        tvAmountLessBank = findViewById(R.id.amount_less_bank_acc);

        String formattedFee = GECL.formatCurrency(bundle.getString("fee"), "");
        String formattedAmountLess = GECL.formatCurrency(bundle.getString("amountLessBankAcc"), "");

        tvTransType.setText(bundle.getString("transType"));
        tvAmount.setText(bundle.getString("amount"));
        tvTransTime.setText(bundle.getString("transTime"));
        tvTransId.setText(bundle.getString("transId"));
        tvSender.setText(bundle.getString("sender"));
        tvBalanceAfter.setText(bundle.getString("balanceAfter"));
        tvFee.setText(formattedFee);
        tvAmountLessBank.setText(formattedAmountLess);
    }

    private void setEvent() {
    }

}