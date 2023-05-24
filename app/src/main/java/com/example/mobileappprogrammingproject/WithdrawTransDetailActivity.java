package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class WithdrawTransDetailActivity extends AppCompatActivity {
    Button btnWithdraw;
    TextView tvTransType, tvAmount, tvTransTime,
            tvTransId, tvSender, tvReceiver, tvBalanceAfter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.withdraw_trans_detail);
        getData();
        setControl();
        setEvent();
    }
    private void getData(){
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
    }

    private void setControl(){
        btnWithdraw = findViewById(R.id.continue_withdraw);
        tvTransType = findViewById(R.id.text_transfer);
        tvAmount = findViewById(R.id.amount_money);
        tvTransTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        tvSender = findViewById(R.id.wallet_card);
        tvReceiver = findViewById(R.id.withdraw_to);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);

        tvTransType.setText(bundle.getString("transType"));
        tvAmount.setText(bundle.getString("amount"));
        tvTransTime.setText(bundle.getString("transTime"));
        tvTransId.setText(bundle.getString("transId"));
        tvSender.setText(bundle.getString("sender"));
        tvReceiver.setText(bundle.getString("receiver"));
        tvBalanceAfter.setText(bundle.getString("balanceAfter"));
    }

    private void setEvent(){
        setBtnWithdrawEvent();
    }

    private void setBtnWithdrawEvent(){
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePage = new Intent(WithdrawTransDetailActivity.this, WithdrawActiveActivity.class);
                startActivity(movePage);
            }
        });
    }
}