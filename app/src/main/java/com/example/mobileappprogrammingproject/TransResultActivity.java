package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.formatCurrency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class TransResultActivity extends AppCompatActivity {
    Button btnBackHome, btnNewWithdraw;
    TextView tvAmount, tvAmount2, tvTime, tvTransId;
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.trans_result);
        setControl();
        getData_RenderView();
        setEvent();
    }

    private void getData_RenderView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String amount = bundle.getString("amount");
        String time = bundle.getString("time");
        String transId = bundle.getString("transId");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime datetime = LocalDateTime.parse(time, dateFormatter);
        String timeStr = datetime.format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"));

        tvAmount.setText(formatCurrency(Integer.parseInt(amount), ""));
        tvAmount2.setText(formatCurrency(Integer.parseInt(amount), ""));
        tvTime.setText(timeStr);
        tvTransId.setText(transId);

        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.successful_gif);
            gifImageView.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setControl() {
        btnBackHome = findViewById(R.id.back_homepage);
        btnNewWithdraw = findViewById(R.id.new_withdraw);
        tvAmount = findViewById(R.id.trans_money_success);
        tvAmount2 = findViewById(R.id.trans_money_success_2);
        tvTime = findViewById(R.id.time_trans_detail);
        tvTransId = findViewById(R.id.trans_id);
        gifImageView = findViewById(R.id.check_logo);
    }

    private void setEvent() {
        setBtnNewWithdraw();
        setBtnBackHomeEvent();
    }

    private void setBtnBackHomeEvent() {
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePage = new Intent(TransResultActivity.this, HomePageActivity.class);
                startActivity(movePage);
            }
        });
    }

    private void setBtnNewWithdraw() {
        btnNewWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}