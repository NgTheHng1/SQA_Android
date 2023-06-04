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
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {
    LinearLayout hisTransAct, userInfoAct;
    CardView cardWithdraw, cardPayBill, cardDeposit, cardTransfer;
    String token;
    TextView  tvBalance, tvUserName;
    ImageButton imgBtnUserInfo, imgBtnBalanceStatus;
    String curBalance;
    AutoCompleteTextView inputSearch;
    boolean isBalanceHiden = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.home_page);
        token = GECL.getTokenFromSession(this);

//        GECL.saveTokenToSession("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJTRFQiOiIwOTg1NzU4MzMzIiwiUGFzc3dvcmQiOiIxMjM0NTYiLCJpYXQiOjE2ODUwNzk4MDgsImV4cCI6MTY4NTE2NjIwOH0.c-yD0pSl9xOvTz1b9mgVw04fbTz5GYiH8CCGDpOzaX4", this);
//        GECL.saveObjectToSession("NGUYỄN VĂN THANH", this, "hoten");
//        GECL.saveObjectToSession("1000000", this, "sodu");
//        GECL.saveObjectToSession("0985758333", this, "sdt");

        setControl();
        setEvent();
        renderView();
        Promise.resolve()
                //get user real name
                .then((action, data) -> {
                    tvUserName.setText(GECL.getObjectFromSession(HomePageActivity.this, "hoten").toUpperCase());
                    action.resolve();
//                    Call<String> call = RetrofitClient.getRetroInterface().getUserName(token);
//                    call.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            String responseStr = response.body();
//                            try {
//                                JSONObject jsonObj = new JSONObject(responseStr).getJSONObject("user");
//                                tvUserName.setText(jsonObj.getString("HoTen").toUpperCase());
//                                action.resolve();
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            GECL.print(t.getMessage());
//                        }
//                    });
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

    @Override
    protected void onResume() {
        super.onResume();
        inputSearch.setText("");
        inputSearch.clearFocus();
    }

    private void renderView() {
//        String[] suggestions = new String[]{
//                "Chuyển tiền",
//                "Nạp tiền",
//                "Rút tiền",
//                "Thanh toán hoá đơn"
//        };
//        ItemSuggestAdapter adapter = new ItemSuggestAdapter(HomePageActivity.this, suggestions);
//        inputSearch.setAdapter(adapter);

        List<GECL.SuggestItem> listSuggests = new ArrayList<>();
        listSuggests.add(new GECL.SuggestItem("Chuyển tiền", R.drawable.exchange_arrow));
        listSuggests.add(new GECL.SuggestItem("Nạp tiền", R.drawable.add_money));
        listSuggests.add(new GECL.SuggestItem("Rút tiền", R.drawable.withdraw_money));
        listSuggests.add(new GECL.SuggestItem("Thanh toán hoá đơn", R.drawable.pay_bill));
        listSuggests.add(new GECL.SuggestItem("Lịch sử giao dịch", R.drawable.history_trans_icon_clicked));
        listSuggests.add(new GECL.SuggestItem("Ví của tôi", R.drawable.user_icon_clicked));

        SuggestItemAdapter adapter = new SuggestItemAdapter(this, listSuggests);
        inputSearch.setAdapter(adapter);
    }



    private void setControl(){
        hisTransAct = findViewById(R.id.his_trans_layout);
        userInfoAct = findViewById(R.id.user_info_layout);
        cardWithdraw = findViewById(R.id.card_withdraw);
        cardPayBill = findViewById(R.id.card_pay_bill);
        imgBtnUserInfo = findViewById(R.id.icon_user);
        tvBalance = findViewById(R.id.balance);
        imgBtnBalanceStatus = findViewById(R.id.isBalanceHidden);
        tvUserName = findViewById(R.id.user_name);
        cardDeposit = findViewById(R.id.deposit);
        cardTransfer = findViewById(R.id.transfer);
        inputSearch = findViewById(R.id.input_search);
    }
    private void setEvent(){

        inputSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                GECL.hideKeyboard(HomePageActivity.this);
                inputSearch.clearFocus();
                return true;
            }
            return false;
        });

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

        //Deposit activity
        for (int i = -1; i < cardDeposit.getChildCount(); i++) {
            View view = i != -1 ?  cardDeposit.getChildAt(i) : cardDeposit;
            view.setOnClickListener(view3 -> {
                Intent movePage = new Intent(HomePageActivity.this, naptien_trangchu.class);
                startActivity(movePage);
            });
        }

        //Transfer activity
        for (int i = -1; i < cardTransfer.getChildCount(); i++) {
            View view = i != -1 ?  cardTransfer.getChildAt(i) : cardTransfer;
            view.setOnClickListener(view3 -> {
                Intent movePage = new Intent(HomePageActivity.this, chuyentien_trangchu.class);
                startActivity(movePage);
            });
        }

        //User info activity
        for (int i = -1; i < userInfoAct.getChildCount(); i++) {
            View view = i != -1 ?  userInfoAct.getChildAt(i) : userInfoAct;
            view.setOnClickListener(view4 -> {
                Intent intent = new Intent(HomePageActivity.this, thongtincanhan_main.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(HomePageActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCompat.startActivity(HomePageActivity.this, intent, options.toBundle());
            });
        }
        imgBtnUserInfo.setOnClickListener(view -> {
                Intent movePage = new Intent(HomePageActivity.this, thongtincanhan_main.class);
                startActivity(movePage);
        });
//        tvUserInfo.setOnClickListener(view -> {
//                Intent movePage = new Intent(HomePageActivity.this, thongtincanhan_main.class);
//                startActivity(movePage);
//        });

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