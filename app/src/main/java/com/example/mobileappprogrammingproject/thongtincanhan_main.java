package com.example.mobileappprogrammingproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.example.mobileappprogrammingproject.APIResult.GetCardBankbyUserResult;
import com.example.mobileappprogrammingproject.APIResult.GetUserbyTokenResult;
import com.example.mobileappprogrammingproject.ObjectJSON.TaiKhoanNganHangJSONObject;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class thongtincanhan_main extends AppCompatActivity {
    Button btnUser, btnClose, btnDangxuat, btnLkBank;
  
     
      
    ArrayList<TaiKhoanNganHangJSONObject> listNHfromAPI = new ArrayList<>();
    ListView listNganhang;
    LinearLayout hisTransLayout, homePageLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtincanhan_main);
        listNganhang = findViewById(R.id.listNganhang);
        hisTransLayout = findViewById(R.id.his_trans_layout);
        homePageLayout = findViewById(R.id.home_page_layout);
        listNganhang.setEnabled(false);

        btnUser = findViewById(R.id.btnUser);
        String token = GECL.getTokenFromSession(this);
        String sdt = GECL.getObjectFromSession(this, "sdt");
        String hoten = GECL.getObjectFromSession(this, "hoten");

        renderListNganHangFromAPI(token, sdt);

        btnUser.setText(hoten);

        //Home page activity
        for (int i = -1; i < homePageLayout.getChildCount(); i++) {
            View view = i != -1 ?  homePageLayout.getChildAt(i) : homePageLayout;
            view.setOnClickListener(view4 -> {
//                Intent movePage = new Intent(thongtincanhan_main.this, HomePageActivity.class);
//                startActivity(movePage);
                finish();
                Intent intent = new Intent(thongtincanhan_main.this, HomePageActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(thongtincanhan_main.this, R.anim.slide_in_left, R.anim.slide_out_right);
                ActivityCompat.startActivity(thongtincanhan_main.this, intent, options.toBundle());
            });
        }

        //History transactions activity
        for (int i = -1; i < hisTransLayout.getChildCount(); i++) {
            View view = i != -1 ?  hisTransLayout.getChildAt(i) : hisTransLayout;
            view.setOnClickListener(view4 -> {
//                Intent movePage = new Intent(thongtincanhan_main.this, HomePageActivity.class);
//                startActivity(movePage);
                finish();
                Intent intent = new Intent(thongtincanhan_main.this, HisTransActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(thongtincanhan_main.this, R.anim.slide_in_left, R.anim.slide_out_right);
                ActivityCompat.startActivity(thongtincanhan_main.this, intent, options.toBundle());
            });
        }

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Token", token);
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
        //                    Log.e("CODE", rs.getUserJSONObject().getHoten());
                            Intent intent = new Intent(thongtincanhan_main.this, thongtincanhan_chitiet.class);
                            intent.putExtra("sdt", obj.getSdt());
                            intent.putExtra("mail", obj.getMail());
                            intent.putExtra("hoten", obj.getHoten());
                            intent.putExtra("gioitinh", obj.getGioitinh());
                            intent.putExtra("cccd", obj.getCccd());
                            intent.putExtra("token", token);
        //                    Log.e("ERROR", obj.getHoten());
                            startActivity(intent);
        //                    finish();
                        }
                        else {
                            alertDialog(rs.getMessage());
                        }
                    }
                    @Override
                    public void onFailure(Call<GetUserbyTokenResult> call, Throwable t) {
                        Toast.makeText(thongtincanhan_main.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLkBank = findViewById(R.id.btnLkBank);
        btnLkBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thongtincanhan_main.this, lienketNganHang.class);
                intent.putExtra("token", token);
                intent.putExtra("hoten", hoten);
                startActivity(intent);
            }
        });

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDangxuat = findViewById(R.id.btnDangxuat);
        btnDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(thongtincanhan_main.this);
                builder.setMessage("Bạn có muốn đăng xuất tài khoản?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent intent = new Intent(thongtincanhan_main.this, dangNhap.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
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
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(thongtincanhan_main.this, HomePageActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(thongtincanhan_main.this, R.anim.slide_in_left, R.anim.slide_out_right);
        ActivityCompat.startActivity(thongtincanhan_main.this, intent, options.toBundle());
    }
    public void renderListNganHangFromAPI(String token, String sdt) {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("sdt", sdt);
        ArrayList<String> listTenNganHang = new ArrayList<>();
        Call<GetCardBankbyUserResult> call  = RetrofitClient.getRetroInterface().executeGetCardBank(map);
        call.enqueue(new Callback<GetCardBankbyUserResult>() {
            @Override
            public void onResponse(Call<GetCardBankbyUserResult> call, Response<GetCardBankbyUserResult> response) {
                GetCardBankbyUserResult rs = response.body();
                String code = rs.getCode();
                listNHfromAPI = rs.getListTKNH();
                if(code.equals("e000")) {
                    if(listNHfromAPI.size() != 0) {
                        Log.e("TenNH", listNHfromAPI.get(0).getTennh());
                        for (TaiKhoanNganHangJSONObject tknh : listNHfromAPI) {
//                        Log.e("TenNH", tknh.getTennh());
                            listTenNganHang.add(tknh.getTennh() + " - " + tknh.getMatk());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(thongtincanhan_main.this, android.R.layout.simple_list_item_1, listTenNganHang);
                        listNganhang.setAdapter(adapter);
                    }
                    else {
                        String []list = {"Chưa có tài khoản ngân hàng nào"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(thongtincanhan_main.this, android.R.layout.simple_list_item_1, list);
                        listNganhang.setAdapter(adapter);
                    }
                }
                else {
                    alertDialog(rs.getMessage());
                }
            }
            @Override
            public void onFailure(Call<GetCardBankbyUserResult> call, Throwable t) {
                Toast.makeText(thongtincanhan_main.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
    }
}