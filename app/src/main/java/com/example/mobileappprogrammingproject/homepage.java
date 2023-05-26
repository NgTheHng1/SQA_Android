package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homepage extends AppCompatActivity {
    LinearLayout MyValletlayout;
    CardView NaptienLayout, ChuyentienLayout;
    TextView user_name;
  
     
      

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.homepage);  Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String hoten = GECL.getObjectFromSession(this, "hoten");
        String sdt = GECL.getObjectFromSession(this, "sdt");
        String sodu = GECL.getObjectFromSession(this, "sodu");

        Log.e("Token", token);

        user_name = findViewById(R.id.user_name);
        user_name.setText(hoten);

        MyValletlayout = findViewById(R.id.MyValletlayout);
        setMyValletLayout(token, hoten, sdt);

        NaptienLayout = findViewById(R.id.NaptienLayout);
        setNaptienLayout(token, sdt, sodu, hoten);

        ChuyentienLayout = findViewById(R.id.ChuyentienLayout);
        setChuyentienLayout(token, sdt, sodu, hoten);

    }
    private void setMyValletLayout(String token, String hoten, String sdt){
        int viewCount = MyValletlayout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = MyValletlayout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(homepage.this, thongtincanhan_main.class);
                    intent.putExtra("token", token);
                    intent.putExtra("hoten", hoten);
                    intent.putExtra("sdt", sdt);
                    startActivity(intent);
                }
            });
        }
    }
    private void setNaptienLayout(String token, String sdt, String sodu, String hoten){
        int viewCount = NaptienLayout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = NaptienLayout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(homepage.this, naptien_trangchu.class);
                    intent.putExtra("token", token);
                    intent.putExtra("hoten", hoten);
                    intent.putExtra("sdt", sdt);
                    intent.putExtra("sodu", sodu);
                    startActivity(intent);
                }
            });
        }
    }
    private void setChuyentienLayout(String token, String sdt, String sodu, String hoten){
        int viewCount = ChuyentienLayout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = ChuyentienLayout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(homepage.this, chuyentien_trangchu.class);
                    intent.putExtra("token", token);
                    intent.putExtra("hoten", hoten);
                    intent.putExtra("sdt", sdt);
                    intent.putExtra("sodu", sodu);
                    startActivity(intent);
                }
            });
        }
    }
}