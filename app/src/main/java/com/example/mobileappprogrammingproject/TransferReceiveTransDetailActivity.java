package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TransferReceiveTransDetailActivity extends AppCompatActivity {
    TextView tvTransType, tvAmount, tvTransTime, tvTransId,
            tvSender, tvReceiver, tvBalanceAfter, tvPhoneNum, tvMessage;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.transfer_receive_trans_detail);
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
        tvSender = findViewById(R.id.receive_account_name);
        tvReceiver = findViewById(R.id.wallet_card);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);
        tvPhoneNum = findViewById(R.id.receive_phone_number);
        tvMessage = findViewById(R.id.trans_message);

        tvTransType.setText(bundle.getString("transType"));
        tvAmount.setText(bundle.getString("amount"));
        tvTransTime.setText(bundle.getString("transTime"));
        tvTransId.setText(bundle.getString("transId"));
        tvSender.setText(bundle.getString("sender"));
        tvReceiver.setText(bundle.getString("receiver"));
        tvBalanceAfter.setText(bundle.getString("balanceAfter"));
        tvPhoneNum.setText(bundle.getString("phoneNum"));
        tvMessage.setText(bundle.getString("message"));
    }
    private void setEvent(){

    }
}