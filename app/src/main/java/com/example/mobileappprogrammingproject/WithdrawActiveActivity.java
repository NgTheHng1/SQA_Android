package com.example.mobileappprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithdrawActiveActivity extends AppCompatActivity {
    TextView titleLayout;
    Button btnConfrmTrans;
    Bundle bundle;
    RecyclerView recyBankList;
    ListBankViewAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_withdraw);
        getData();
        setRenderingView();
        setControl();
        setEvent();
        Log.d("myclasss", String.valueOf(recyBankList.getChildCount()));
    }
    private void getData(){

    }
    private void setRenderingView(){
        List<Bank> listBank = new ArrayList<>(Arrays.asList(
                new Bank(1, 0, "AGRIBANK", R.drawable.agribank_logo),
                new Bank(2, 0, "Vietcombank", R.drawable.vietcombank_logo),
                new Bank(3, 0, "VietinBank", R.drawable.vietinbank_logo),
                new Bank(4, 0, "TECHCOMBANK", R.drawable.techcombank_logo)
        ));
        recyBankList = findViewById(R.id.recy_list_bank);
        recyBankList.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ListBankViewAdapter(WithdrawActiveActivity.this, listBank);
        recyBankList.setAdapter(myAdapter);

//        for (int i = 0; i < recyBankList.getChildCount(); i++) {
//            ViewGroup viewGroup = (ViewGroup) recyBankList.getChildAt(i);
//            Log.d("myclasss", String.valueOf(viewGroup.getChildCount()) + "123");
//            Log.d("myclasss", "123");
//            for (int j = 0; j < viewGroup.getChildCount(); j++) {
//                Log.d("myclasss", String.valueOf(i) + " " + String.valueOf(j));
//                if(viewGroup.getChildAt(j) instanceof CheckBox){
//                    myAdapter.listBankChBox.add((CheckBox) viewGroup.getChildAt(j));
//                    break;
//                }
//            }
//        }
    }
    private void setControl() {
        btnConfrmTrans = findViewById(R.id.confirm_trans);
        titleLayout = findViewById(R.id.text_withdrw);
    }

    private void setEvent() {
        setConfirmTransEvent();
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup viewGroup = (ViewGroup) recyBankList.getChildAt(0);
                Toast.makeText(WithdrawActiveActivity.this, String.valueOf(viewGroup.getChildCount()), Toast.LENGTH_SHORT).show();
            }
        });
//        titleLayout.performClick();
    }

    private void setConfirmTransEvent() {
        btnConfrmTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent movePage = new Intent(WithdrawActiveActivity.this, ConfirmWithdrawActivity.class);
//                startActivity(movePage);
//                RecyclerView recyBankList = findViewById(R.id.recy_list_bank);
                Toast.makeText(WithdrawActiveActivity.this, String.valueOf(myAdapter.checkedPosition), Toast.LENGTH_SHORT).show();
            }
        });
    }

}