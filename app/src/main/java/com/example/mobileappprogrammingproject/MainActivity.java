package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout his_trans_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setControl();
        setEvent();
    }

    private void setControl(){
        his_trans_layout = findViewById(R.id.his_trans_layout);
    }

    private void setEvent(){
        setHisTransBtnEvent();
    }

    private void setHisTransBtnEvent(){
        int viewCount = his_trans_layout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = his_trans_layout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent movePage = new Intent(MainActivity.this, HisTransActivity.class);
                    startActivity(movePage);
                }
            });
        }
    }
}