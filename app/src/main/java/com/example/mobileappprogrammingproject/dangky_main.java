package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.SignupResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class dangky_main extends AppCompatActivity {
    Button btnDangky;
    Button btnClose;
    EditText txtsdt;
    EditText txtpassword;
    EditText txtname;
    EditText txtCCCD;
    RadioGroup radioGroup;
    RadioButton gender_male;
    RadioButton radioBtnChecked;
    EditText txtemail;
  
     
      

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.dangky_main);
         

        txtsdt = findViewById(R.id.txtsdt);
        txtpassword = findViewById(R.id.txtpassword);
        txtname = findViewById(R.id.txtname);
        txtemail = findViewById(R.id.txtemail);
        txtCCCD = findViewById(R.id.txtCCCD);
        radioGroup = findViewById(R.id.radioGroup);
        gender_male = findViewById(R.id.gender_male);
        gender_male.setChecked(true);
        btnDangky = findViewById(R.id.btnDangky);

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioBtnChecked = findViewById(radioId);
                Log.i("Checked", radioBtnChecked.getText().toString());
                handleSignUp(radioBtnChecked.getText().toString());
            }
        });

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangky_main.this, dangNhap.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public Integer getGioiTinh(String gender) {
        if(gender.equals("Nam")) {
            return 0;
        }
        else {
            return 1;
        }
    }
    public void handleSignUp(String str) {
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("SDT", txtsdt.getText().toString());
                map.put("Mail", txtemail.getText().toString());
                map.put("Password", txtpassword.getText().toString());
                map.put("HoTen", txtname.getText().toString());
                map.put("GioiTinh", getGioiTinh(str).toString());
                map.put("CCCD", txtCCCD.getText().toString());

                Call<SignupResult> call  = RetrofitClient.getRetroInterface().executeSignup(map);
                call.enqueue(new Callback<SignupResult>() {
                    @Override
                    public void onResponse(Call<SignupResult> call, Response<SignupResult> response) {
                        SignupResult rs = response.body();
                        if(rs.getSuccess() == false && rs.getCode().equals("e001")) {
                            alertDialog(rs.getMessage());
                        }
                        else if(rs.getSuccess() == false && rs.getCode().equals("e002")) {
                            alertDialog(rs.getMessage());
                        }
                        else if(rs.getSuccess() == true && rs.getCode().equals("e000")) {
                            alertDialog(rs.getMessage());
                            Intent intent = new Intent(dangky_main.this, dangky_thanhcong.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignupResult> call, Throwable t) {
                        Toast.makeText(dangky_main.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR", t.getMessage());
                    }
                });
            }
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
    }
}