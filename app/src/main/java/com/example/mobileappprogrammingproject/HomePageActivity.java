package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {
    LinearLayout hisTransAct, userInfoAct;
    CardView cardWithdraw, cardPayBill;
    String token;
    TextView tvUserInfo, tvBalance, tvUserName;
    ImageButton imgBtnUserInfo, imgBtnBalanceStatus;
    String curBalance;
    boolean isBalanceHiden = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));


//        GECL.saveTokenToSession(getIntent().getBundleExtra("bundle").getString("token"), this);
        GECL.saveTokenToSession("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJTRFQiOiIwOTg1NzU4MzMzIiwiUGFzc3dvcmQiOiIzNDU0NTQiLCJpYXQiOjE2ODQ5MDUzNjUsImV4cCI6MTY4NDk5MTc2NX0.YQN1u43eUvXkZ7kGfHHyQjR-uhP6eRkZle0fVLcMvHQ",
                this);
        token = GECL.getTokenFromSession(this);

        setContentView(R.layout.home_page);
        setControl();
        setEvent();
        Promise.resolve()
                //get user real name
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getUserName(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr).getJSONObject("user");
                                tvUserName.setText(jsonObj.getString("HoTen").toUpperCase());
                                action.resolve();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.print(t.getMessage());
                        }
                    });
                })
                //get balance of user
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                curBalance = jsonObj.getJSONObject("balance").getString("SoDu");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", HomePageActivity.this);
                            action.reject();
                        }
                    });
                })
                .start();
    }

    private void setControl(){
        hisTransAct = findViewById(R.id.his_trans_layout);
        userInfoAct = findViewById(R.id.user_info_layout);
        cardWithdraw = findViewById(R.id.card_withdraw);
        cardPayBill = findViewById(R.id.card_pay_bill);
        imgBtnUserInfo = findViewById(R.id.icon_user);
        tvUserInfo = findViewById(R.id.txt_user);
        tvBalance = findViewById(R.id.balance);
        imgBtnBalanceStatus = findViewById(R.id.isBalanceHidden);
        tvUserName = findViewById(R.id.user_name);
    }
    private void setEvent(){
        //History transaction activity
        for (int i = -1; i < hisTransAct.getChildCount(); i++) {
            View view = i != -1 ?  hisTransAct.getChildAt(i) : hisTransAct;
            view.setOnClickListener(view1 -> {
                Intent intent = new Intent(HomePageActivity.this, HisTransActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(HomePageActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCompat.startActivity(HomePageActivity.this, intent, options.toBundle());

            });
        }

        //Withdraw activity
        for (int i = -1; i < cardWithdraw.getChildCount(); i++) {
            View view = i != -1 ?  cardWithdraw.getChildAt(i) : cardWithdraw;
            view.setOnClickListener(view2 -> {
                Intent movePage = new Intent(HomePageActivity.this, WithdrawActiveActivity.class);
                startActivity(movePage);
            });
        }

        //Pay bill activity
        for (int i = -1; i < cardPayBill.getChildCount(); i++) {
            View view = i != -1 ?  cardPayBill.getChildAt(i) : cardPayBill;
            view.setOnClickListener(view3 -> {
                Intent movePage = new Intent(HomePageActivity.this, PayBillViewActivity.class);
                startActivity(movePage);
            });
        }

        //User info activity
        for (int i = -1; i < userInfoAct.getChildCount(); i++) {
            View view = i != -1 ?  userInfoAct.getChildAt(i) : userInfoAct;
            view.setOnClickListener(view4 -> {
//                Intent movePage = new Intent(HomePageActivity.this, HisTransActivity.class);
//                startActivity(movePage);
            });
        }
        imgBtnUserInfo.setOnClickListener(view -> {
//                Intent movePage = new Intent(HomePageActivity.this, HisTransActivity.class);
//                startActivity(movePage);
        });
        tvUserInfo.setOnClickListener(view -> {
//                Intent movePage = new Intent(HomePageActivity.this, HisTransActivity.class);
//                startActivity(movePage);
        });

        imgBtnBalanceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBalanceHiden) {
                    tvBalance.setText(GECL.formatCurrency(curBalance, ""));
                    imgBtnBalanceStatus.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.visibible_icon));
                }else {
                    tvBalance.setText("************");
                    imgBtnBalanceStatus.setImageDrawable(ContextCompat.getDrawable(HomePageActivity.this, R.drawable.invisible_icon));
                }

                isBalanceHiden = !isBalanceHiden;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}