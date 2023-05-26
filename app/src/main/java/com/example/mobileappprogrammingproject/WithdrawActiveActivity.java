package com.example.mobileappprogrammingproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActiveActivity extends AppCompatActivity {
    TextView tvBalance;
    Button btnConfrmTrans;
    ImageButton imgBtnClose;
    Bundle bundle;
    EditText edtText;
    RecyclerView recyBankList;
    ListBankViewAdapter myAdapter;
    List<Bank> listBank;
    RetrofitClient.RetrofitInterface apiService;
    String token;
    public int curBalance, transAmount, bankOrderSelected;
    HashMap<Integer, List<String>> bankOrderToSTK;
    boolean isActivityPaused = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.money_withdraw);
        apiService = RetrofitClient.getRetroInterface();
        listBank = new ArrayList<>();
        curBalance = 1000000;
        transAmount = 0;
        bankOrderSelected = 0;
        bankOrderToSTK = new HashMap<>();
        token = GECL.getTokenFromSession(this);

        setControl();
        setEvent();
        getData_RenderView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityPaused){
            isActivityPaused = false;
            edtText.getText().clear();
        }
    }

    @Override
    public void onBackPressed() {
        if(edtText.hasFocus())
            edtText.clearFocus();
        else
            super.onBackPressed();
    }

    private void getData_RenderView(){

        Func getCusBankList = (action, data) -> {
            Call<String> call = apiService.getCardBankByUser(token);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        String responseStr = response.body();
                        JSONObject jsonObj = new JSONObject(responseStr);
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonEle = (JSONObject) jsonArray.get(i);
                            String bankName = jsonEle.getString("TENNH");
                            int bankId = jsonEle.getInt("MANH");
                            int fee = jsonEle.getInt("CHIETKHAU");
                            String bankAccId = jsonEle.getString("MATK");

                            bankOrderToSTK.put(i, Arrays.asList(bankAccId, bankName));

                            listBank.add(new Bank(bankId, fee, bankName, Bank.getBankSourceImg()[bankId]));
                        }
                            action.resolve();

                    } catch (JSONException e) {
                        action.reject();
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    action.reject();
                }
            });
        };

        Promise.resolve()
                .then((action, data) -> {
                    Call<String> call = apiService.getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                String balance = jsonObj.getJSONObject("balance").getString("SoDu");
                                curBalance = Integer.parseInt(balance);
                                tvBalance.setText(GECL.formatCurrency(balance, ""));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", WithdrawActiveActivity.this);
                            action.reject();
                        }
                    });
                })
                .then(getCusBankList)
                .then((action, data) -> {
                    recyBankList = findViewById(R.id.recy_list_bank);
                    recyBankList.setLayoutManager(new LinearLayoutManager(this));
                    myAdapter = new ListBankViewAdapter(WithdrawActiveActivity.this, listBank);
                    recyBankList.setAdapter(myAdapter);
                    action.resolve();
                })
                .start();

    }
    private void setControl() {
        btnConfrmTrans = findViewById(R.id.confirm_trans);
        edtText = findViewById(R.id.withdraw_edit_text);
        tvBalance = findViewById(R.id.balance_money);
        imgBtnClose = findViewById(R.id.close_icon);
    }

    private void setEvent() {
        setConfirmTransEvent();

        edtText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                String formattedAmount = edtText.getText().toString().replaceAll("\\D+", "");
                int tmpAmount = formattedAmount.equals("") ? 0 : Integer.parseInt(formattedAmount);
                if(tmpAmount > curBalance)
                    Toast.makeText(this, "Số tiền rút nhiều hơn số tiền trong ví", Toast.LENGTH_SHORT).show();

                transAmount = tmpAmount;
                hideKeyboard(this);
                edtText.clearFocus();
                return true;
            }
            return false;
        });

        edtText.addTextChangedListener(new MoneyTextWatcher(edtText));

        edtText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    edtText.setBackground(ContextCompat.getDrawable(WithdrawActiveActivity.this, R.drawable.withdraw_input_focus));
                }else{
                    edtText.setBackground(ContextCompat.getDrawable(WithdrawActiveActivity.this, R.drawable.withdraw_input));
                }
            }
        });
        GECL.setCloseBtnEvent(this, imgBtnClose);
    }
    private void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private boolean isEveryRequireOk(){
        if(transAmount < 50000){
            AlertDialog.makeDialog("Số tiền rút phải nhiều hơn 50.000đ", this);
            return false;
        }
        if(transAmount > curBalance){
            AlertDialog.makeDialog("Số tiền rút nhiều hơn số tiền trong ví", this);
            return false;
        }
        if(bankOrderSelected == -1){
            AlertDialog.makeDialog("Bạn chưa chọn ngân hàng rút tiền về", this);
            return false;
        }
        return true;
    }

    private void setConfirmTransEvent() {
        btnConfrmTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEveryRequireOk())
                    return;
//                Toast.makeText(WithdrawActiveActivity.this,
//                bankOrderToSTK.get(bankOrderSelected).get(0) + " - " +
//                        bankOrderToSTK.get(bankOrderSelected).get(1) + " - " + transAmount
//                    , Toast.LENGTH_SHORT).show()
                Bundle bundle = new Bundle();
                bundle.putInt("amount", transAmount);
                bundle.putString("bankAcc", bankOrderToSTK.get(bankOrderSelected).get(0));
                bundle.putString("bankName", bankOrderToSTK.get(bankOrderSelected).get(1));
                bundle.putString("token", token);

                Intent movePage = new Intent(WithdrawActiveActivity.this, ConfirmWithdrawActivity.class);
                movePage.putExtra("bundle", bundle);
                startActivity(movePage);
            }
        });
    }

    public class MoneyTextWatcher implements TextWatcher {
        private final WeakReference<EditText> editTextWeakReference;

        public MoneyTextWatcher(EditText editText) {
            editTextWeakReference = new WeakReference<EditText>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            try{
                if (editText == null) return;
                String s = editable.toString();
                if (s.isEmpty()) return;
                editText.removeTextChangedListener(this);
                String cleanString = s.replaceAll("\\D+", "");
                DecimalFormat currencyFormatter = new DecimalFormat("#,###");
                String formatted = currencyFormatter.format((double) Integer.parseInt(cleanString));

                editText.setText(formatted);
                editText.setSelection(formatted.length());
                editText.addTextChangedListener(this);
            }catch(NumberFormatException e){
                String str = editable.toString();
                str = str.substring(0,str.length()-1);
                editText.setText(str);
                editText.setSelection(str.length());
                editText.addTextChangedListener(this);
            }
        }
    }
}
