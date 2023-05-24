package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class TransferTransDetailActivity extends AppCompatActivity {

    TextView tvTransType, tvAmount, tvTransTime, tvTransId,
            tvSender, tvReceiver, tvBalanceAfter, tvPhoneNum, tvMessage,
            tvFee, tvActualReceive;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.transfer_trans_detail);
        getData();
        setControl();
        setEvent();
    }
    private void getData(){
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
    }
    private void setControl(){
        tvTransType = findViewById(R.id.text_transfer);
        tvAmount = findViewById(R.id.amount_money);
        tvTransTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        tvSender = findViewById(R.id.wallet_card);
        tvReceiver = findViewById(R.id.receive_account_name);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);
        tvPhoneNum = findViewById(R.id.receive_phone_number);
        tvMessage = findViewById(R.id.trans_message);
        tvFee = findViewById(R.id.trans_fee);
        tvActualReceive = findViewById(R.id.actual_amount);

        String frtdFee = GECL.formatCurrency(bundle.getString("fee"), "");
        String frtdActualAmount = GECL.formatCurrency(bundle.getString("actualAmount"), "");

        tvTransType.setText(bundle.getString("transType"));
        tvAmount.setText(bundle.getString("amount"));
        tvTransTime.setText(bundle.getString("transTime"));
        tvTransId.setText(bundle.getString("transId"));
        tvSender.setText(bundle.getString("sender"));
        tvReceiver.setText(bundle.getString("receiver"));
        tvBalanceAfter.setText(bundle.getString("balanceAfter"));
        tvPhoneNum.setText(bundle.getString("phoneNum"));
        tvMessage.setText(bundle.getString("message"));
        tvFee.setText(frtdFee);
        tvActualReceive.setText(frtdActualAmount);
    }
    private void setEvent(){

    }
}