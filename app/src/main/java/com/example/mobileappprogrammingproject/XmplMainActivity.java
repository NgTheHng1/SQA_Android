package com.example.mobileappprogrammingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XmplMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_transaction);

        RecyclerView recyclerView = findViewById(R.id.recy_total_trans_his);

        Bank  Agribank = new Bank(1, 0.01, "Agribank",R.drawable.agribank_logo);
        //gọi api lại mõi khi cần thông tin tài khoản được liên kết phải cập nhật real-time
        BankAccount myBankAccount = new BankAccount("AGR001", 1000000, "0390102035", Agribank);

        List<List<Transactions>> items = new ArrayList<>();
        items.add(Arrays.asList(
                new BankTrans("W001", 30000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_WITHDRAW),
                new TransferTrans("T001", 60000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_RECEIVE, "Chuyển tiền mua thiết bị"),
                new BankTrans("W001", 30000, LocalDateTime.of(2023, 3, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_WITHDRAW)
        ));
        items.add(Arrays.asList(
                new TransferTrans("T001", 60000, LocalDateTime.of(2023, 4, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_SEND, "Chuyển tiền mua thiết bị"),
                new TransferTrans("T001", 60000, LocalDateTime.of(2023, 4, 16, 20, 50, 30), 500000, new Account("03681111111", "s@gmail.com", new User("Nguyễn Cảnh", null)), Transactions.TRANSFER_SEND, "Chuyển tiền mua thiết bị")
        ));
        items.add(Arrays.asList(
                new BankTrans("W001", 30000, LocalDateTime.of(2023, 5, 16, 20, 50, 30), 500000, myBankAccount, Transactions.BANK_DEPOSIT)
        ));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TotalTransViewAdapter(XmplMainActivity.this, items));
    }
}