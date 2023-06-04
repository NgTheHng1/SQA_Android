package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.BasicResult;

import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thongtincanhan_chitiet_editcanhan extends AppCompatActivity {
    Button btnClose, btnCapnhat;
    EditText txtHoten;
    EditText txtCCCD;
    EditText txtEmail;
    Spinner spnGioiTinh;
  
     
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.thongtincanhan_chitiet_editcanhan);  btnClose = findViewById(R.id.btnClose);
        btnCapnhat = findViewById(R.id.btnCapnhat);
        txtHoten = findViewById(R.id.txtHoten);
        spnGioiTinh = findViewById(R.id.txtGioiTinh);
        txtCCCD = findViewById(R.id.txtCCCD);
        txtEmail = findViewById(R.id.email);

        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String gender = intent.getStringExtra("gioitinh");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String cccd = intent.getStringExtra("cccd");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        String email = intent.getStringExtra("email");

        txtHoten.setText(hoten);
        txtEmail.setText(email);
//        if (gender.equals("1")) {
//            spnGioiTinh.setText("Nữ");
//        }
//        else {
//            spnGioiTinh.setText("Nam");
//        }

        txtCCCD.setText(cccd);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Nam", "Nữ"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);
        spnGioiTinh.setSelection(Integer.parseInt(gender));

        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                if(email.equals("")){
                    alertDialog("Không để trống ô email!");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    alertDialog("Email nhập vào không đúng định dạng!");
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("GioiTinh", String.valueOf(spnGioiTinh.getSelectedItemPosition()));
                map.put("Email", txtEmail.getText().toString());

                Call<BasicResult> call  = RetrofitClient.getRetroInterface().excuteUpdateUser(map);
                call.enqueue(new Callback<BasicResult>() {
                    @Override
                    public void onResponse(Call<BasicResult> call, Response<BasicResult> response) {
                        BasicResult rs = response.body();
                        String code = rs.getCode();
                        if(code.equals("e000")) {
                            Intent intent = new Intent(thongtincanhan_chitiet_editcanhan.this, HomePageActivity.class);
                            intent.putExtra("hoten", txtHoten.getText().toString());
                            intent.putExtra("token", token);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            alertDialog(rs.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResult> call, Throwable t) {
                    }
                });
            }
        });

//        btnCapnhat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("token", token);
//                map.put("HoTen", txtHoten.getText().toString());
//                map.put("GioiTinh", String.valueOf(spnGioiTinh.getSelectedItemPosition()));
//                map.put("CCCD", txtCCCD.getText().toString());
//
//                Call<BasicResult> call  = RetrofitClient.getRetroInterface().excuteUpdateUser(map);
//                call.enqueue(new Callback<BasicResult>() {
//                    @Override
//                    public void onResponse(Call<BasicResult> call, Response<BasicResult> response) {
//                        BasicResult rs = response.body();
//                        String code = rs.getCode();
//                        if(code.equals("e000")) {
//                            Intent intent = new Intent(thongtincanhan_chitiet_editcanhan.this, HomePageActivity.class);
//                            intent.putExtra("hoten", txtHoten.getText().toString());
//                            intent.putExtra("token", token);
//                            startActivity(intent);
//                            finish();
//
//                        }
//                        else {
//                            alertDialog(rs.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BasicResult> call, Throwable t) {
//                    }
//                });
//            }
//        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
    }
    public String changeType(String gender) {
        if (gender.equals("Nữ")) {
            return "1";
        }
        else {
            return "0";
        }
    }
}