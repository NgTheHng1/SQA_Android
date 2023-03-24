package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HisTransActivity extends AppCompatActivity {;
    ImageButton btnBack;
    Button btnAllTrans, btnDepositTrans, btnWithTrans, btnReceiveTrans, btnTransferTrans;
    TextView titleLayout;
    List<List<Transactions>> listTrans;
    List<Integer> myList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_transaction);
        getData();
        setControl();
        setEvent();
    }
    private void getData(){

        new HttpAsyncTask(new HttpAsyncTask.TaskCompleteListener<String>() {
            @Override
            public void onTaskComplete(String result) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Integer>>(){}.getType();
//                List<Integer> list = gson.fromJson(result, listType);
//                Toast.makeText(HisTransActivity.this, String.valueOf(list.get(0)), Toast.LENGTH_SHORT).show();

                Bank  Agribank = new Bank(1, 0.01, "Agribank",R.drawable.agribank_logo);
                //gọi api lại mõi khi cần thông tin tài khoản được liên kết phải cập nhật real-time
                BankAccount myBankAccount = new BankAccount("AGR001", 1000000, "0390102035", Agribank);

                listTrans = new ArrayList<>();
                listTrans.add(Arrays.asList(
                        new BankTrans("W001", 30000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_WITHDRAW),
                        new TransferTrans("T001", 60000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_RECEIVE, "Chuyển tiền mua thiết bị"),
                        new BankTrans("W001", 30000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_WITHDRAW)
                ));
                listTrans.add(Arrays.asList(
                        new TransferTrans("T001", 60000, LocalDateTime.of(2023, 4, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_SEND, "Chuyển tiền mua thiết bị"),
                        new TransferTrans("T001", 60000, LocalDateTime.of(2023, 4, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_SEND, "Chuyển tiền mua thiết bị")
                ));
                listTrans.add(Arrays.asList(
                        new BankTrans("W001", 30000, LocalDateTime.of(2023, 5, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_DEPOSIT)
                ));

                renderView();
            }
        }).execute(HttpAsyncTask.getRoute("transactions"));
//    }).execute(HttpAsyncTask.getRoute("transactions"));
    }
    private void renderView(){
        recyclerView = findViewById(R.id.recy_total_trans_his);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listTrans));
    }

    private void setControl() {
        btnBack = findViewById(R.id.back_btn);
        btnAllTrans = findViewById(R.id.btn_trans_all);
        btnDepositTrans = findViewById(R.id.btn_trans_deposit);
        btnWithTrans = findViewById(R.id.btn_trans_withdraw);
        btnReceiveTrans = findViewById(R.id.btn_trans_receive);
        btnTransferTrans = findViewById(R.id.btn_trans_transfer);
        titleLayout = findViewById(R.id.title_layout);
    }
    public void makeToast(int i){
        Toast.makeText(HisTransActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
    }

    private void setEvent() {
        setBtnBackEvent();
        btnAllTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiliButton(btnAllTrans);
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listTrans));
            }
        });
        btnDepositTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiliButton(btnDepositTrans);
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listFilteredTrans(Transactions.BANK_DEPOSIT)));
            }
        });
        btnWithTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiliButton(btnWithTrans);
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listFilteredTrans(Transactions.BANK_WITHDRAW)));
            }
        });
        btnTransferTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiliButton(btnTransferTrans);
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listFilteredTrans(Transactions.TRANSFER_SEND)));
            }
        });
        btnReceiveTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiliButton(btnReceiveTrans);
                recyclerView.setAdapter(new TotalTransViewAdapter(HisTransActivity.this, listFilteredTrans(Transactions.TRANSFER_RECEIVE)));
            }

        });
    }
    private List<List<Transactions>> listFilteredTrans(int transType){
        List<List<Transactions>> listResult = new ArrayList<>();
        for (int i = 0; i < listTrans.size(); i++) {
            List<Transactions> tmpFilteredList = new ArrayList<>();
            List<Transactions> tmpCheckingList = listTrans.get(i);
            for (int j = 0; j < tmpCheckingList.size(); j++) {
                Transactions transaction = tmpCheckingList.get(j);
                if(transaction.getTransType() == transType){
                    tmpFilteredList.add(transaction);
                }
            }
            if(tmpFilteredList.size() != 0)
                listResult.add(tmpFilteredList);
        }
        if(listResult.size() == 0) {
            Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
            return listTrans;
        }
        return listResult;
    }
    void hiliButton(Button btn){
        btn.setBackgroundResource(R.drawable.clicked_button);
        btn.setTextColor(ContextCompat.getColor(HisTransActivity.this, R.color.app_color));

        int[] listIdButton = new int[]{
                R.id.btn_trans_all,
                R.id.btn_trans_receive,
                R.id.btn_trans_transfer,
                R.id.btn_trans_deposit,
                R.id.btn_trans_withdraw
        };
        for(int btnId : listIdButton){
            if(btnId == btn.getId())
                continue;
            Button btnTmp = findViewById(btnId);
            btnTmp.setBackgroundResource(R.drawable.unclicked_button);
            btnTmp.setTextColor(ContextCompat.getColor(HisTransActivity.this, R.color.black));
        }
    }

    private void setBtnBackEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}