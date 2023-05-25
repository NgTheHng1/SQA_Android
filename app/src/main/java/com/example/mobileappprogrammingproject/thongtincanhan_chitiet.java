package com.example.mobileappprogrammingproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappprogrammingproject.APIResult.GetUserbyTokenResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class thongtincanhan_chitiet extends AppCompatActivity {
    TextView txtHoten;
    TextView txtCCCD;
    TextView txtEmail;
    TextView txtGioitinh;

  
     
      
    Button btnClose, btnChinhsuacanhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtincanhan_chitiet);  txtHoten = findViewById(R.id.txtHoten);
        txtGioitinh = findViewById(R.id.txtGioitinh);
        txtCCCD = findViewById(R.id.txtCCCD);
        txtEmail = findViewById(R.id.txtEmail);
        Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String gender = intent.getStringExtra("gioitinh");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String cccd = intent.getStringExtra("cccd");
        String mail = intent.getStringExtra("mail");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        txtHoten.setText(hoten);
        if (gender.equals("1")) {
            txtGioitinh.setText("Nữ");
        }
        else {
            txtGioitinh.setText("Nam");
        }
        txtCCCD.setText(cccd);
        txtEmail.setText(mail);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChinhsuacanhan = findViewById(R.id.btnChinhsuacanhan);
        btnChinhsuacanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                Call<GetUserbyTokenResult> call  = RetrofitClient.getRetroInterface().executeGetUser(map);
                call.enqueue(new Callback<GetUserbyTokenResult>() {
                    @Override
                    public void onResponse(Call<GetUserbyTokenResult> call, Response<GetUserbyTokenResult> response) {
                        GetUserbyTokenResult rs = response.body();
                        String code = rs.getCode();
                        if(code.equals("e000")) {
                            UserJSONObject obj = rs.getUserJSONObject();
                            Log.e("CODE", code);
                            Intent intent = new Intent(thongtincanhan_chitiet.this, thongtincanhan_chitiet_editcanhan.class);
                            intent.putExtra("hoten", obj.getHoten());
                            intent.putExtra("gioitinh", obj.getGioitinh());
                            intent.putExtra("cccd", obj.getCccd());
                            intent.putExtra("sdt", sdt);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                        else {
                            alertDialog(rs.getMessage());
                        }
                    }
                    @Override
                    public void onFailure(Call<GetUserbyTokenResult> call, Throwable t) {
                        Toast.makeText(thongtincanhan_chitiet.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}