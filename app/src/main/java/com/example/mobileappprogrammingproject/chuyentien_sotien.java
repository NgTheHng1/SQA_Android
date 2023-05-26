package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.hideKeyboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.TransferResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chuyentien_sotien extends AppCompatActivity {
    TextView tenNguoiNhan, sdtNguoiNhan;
    EditText txtSotien, txtLoinhan;
    Button btnAccept, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.chuyentien_sotien);  Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String userHoten = intent.getStringExtra("userHoten");
        String userSdt = intent.getStringExtra("userSdt");
        String adminHoten = intent.getStringExtra("adminHoten");
        String adminSDT = intent.getStringExtra("adminSDT");
        tenNguoiNhan = findViewById(R.id.tenNguoiNhan);
        tenNguoiNhan.setText(userHoten);
        sdtNguoiNhan = findViewById(R.id.sdtNguoiNhan);
        sdtNguoiNhan.setText(userSdt);

        txtSotien = findViewById(R.id.txtSotien);
        txtLoinhan = findViewById(R.id.txtLoinhan);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            //sotien, tên receiver, sdt ngnhan, message
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("sotien", txtSotien.getText().toString());
                bundle.putString("hoten_ngnhan", userHoten);
                bundle.putString("sdt_ngnhan", userSdt);
                bundle.putString("message", txtLoinhan.getText().toString());
                startActivity(new Intent(chuyentien_sotien.this, chuyentien_confirm.class)
                        .putExtra("bundle", bundle));

//                HashMap<String, String> map = new HashMap<>();
//                map.put("token", token);
//                map.put("STGD", txtSotien.getText().toString());
//                map.put("SDT", userSdt);
//                map.put("loiNhan", txtLoinhan.getText().toString());
//
//                Call<TransferResult> call  = RetrofitClient.getRetroInterface().executeTransferResult(map);
//                call.enqueue(new Callback<TransferResult>() {
//                    @Override
//                    public void onResponse(Call<TransferResult> call, Response<TransferResult> response) {
//                        TransferResult rs = response.body();
//                        String code = rs.getCode();
//                        String message = rs.getMessage();
//                        UserJSONObject user = rs.getUser();
//                        String magd = rs.getMaGD();
//                        String sodu = rs.getSodu();
//                        if (code.equals("e000")) {
//                            Intent intent = new Intent(chuyentien_sotien.this, chuyentien_thanhcong.class);
//                            intent.putExtra("sodu", sodu);
//                            intent.putExtra("token", token);
//                            intent.putExtra("sdt", user.getSdt());//
//                            intent.putExtra("hoten", user.getHoten());//
//                            intent.putExtra("magd", magd);
//                            intent.putExtra("loinhan", txtLoinhan.getText().toString());
//                            intent.putExtra("sotien", txtSotien.getText().toString());
//                            intent.putExtra("adminHoten", adminHoten);
//                            intent.putExtra("adminSDT", adminSDT);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            alertDialog(message);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<TransferResult> call, Throwable t) {
//                        Toast.makeText(chuyentien_sotien.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
        txtSotien.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                hideKeyboard(this);
                txtSotien.clearFocus();
                return true;
            }
            return false;
        });
    }
    public void alertDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
    }
}