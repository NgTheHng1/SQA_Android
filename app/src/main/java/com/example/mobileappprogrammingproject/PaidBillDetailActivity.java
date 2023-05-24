package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaidBillDetailActivity extends AppCompatActivity {
    ImageView ivTransType;
    TextView tvTextDetail, tvAmount, tvTime,
            tvTransId, tvBalanceAfter, tvBillName, tvComName;
    Button btnNewPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.pay_bill_detail);

        setControl();
        renderView();
        setEvent();
    }

    private void setEvent() {
        btnNewPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getBundleExtra("bundle").getString("isBalanceAfter").equals("false"))
                    onBackPressed();
                else
                    PaidBillDetailActivity.this.startActivity(
                            new Intent(PaidBillDetailActivity.this, PayBillViewActivity.class)
                    );
            }
        });
    }

    private void renderView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");

        ivTransType.setImageResource(Transactions.getImgSourceDetail()[bundle.getInt("transType")]);
        tvAmount.setText(GECL.formatCurrency(bundle.getInt("amount"), ""));
        tvTime.setText(bundle.getString("time"));
        tvTransId.setText(bundle.getString("transId"));
        tvBalanceAfter.setText(GECL.formatCurrency(bundle.getInt("balanceAfter"), ""));
        tvBillName.setText(bundle.getString("billName"));
        tvComName.setText(bundle.getString("comName"));
    }

    private void setControl() {
        ivTransType = findViewById(R.id.trans_type_image);
        tvTextDetail = findViewById(R.id.text_detail);
        tvAmount = findViewById(R.id.amount_money);
        tvTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        tvBalanceAfter = findViewById(R.id.balance_after_trans);
        tvBillName = findViewById(R.id.bill_name);
        tvComName = findViewById(R.id.ser_company);
        btnNewPay = findViewById(R.id.continue_pay);

        if(getIntent().getBundleExtra("bundle").getString("isBalanceAfter").equals("false")){
            tvBalanceAfter.setVisibility(View.GONE);
            findViewById(R.id.text6).setVisibility(View.GONE);

            TextView txt7 = findViewById(R.id.text7);
            float scale = getResources().getDisplayMetrics().density;
            int marginTopInDp = 175;
            int marginTopInPx = (int) (marginTopInDp * scale + 0.5f);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txt7.getLayoutParams();
            layoutParams.setMargins(0, marginTopInPx, 0, 0);
            txt7.setLayoutParams(layoutParams);
        }
    }
}