package com.example.mobileappprogrammingproject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.GetAllBanksResult;
import com.example.mobileappprogrammingproject.APIResult.LinkBankResult;
import com.example.mobileappprogrammingproject.ObjectJSON.NganHangJSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class lienketNganHang extends AppCompatActivity {
    Button btnAccept, btnClose;
    ImageButton btnDatepicker;
    TextView txtDate;
    EditText txtSothe, txtChuthe, txtCCCD;
    Spinner spinnerNganHang;
    ArrayList<NganHangJSONObject> listNH = new ArrayList<>();
    String manh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(com.example.mobileappprogrammingproject.R.layout.lienket_nganhang);  Intent intent = getIntent();
        String token = GECL.getObjectFromSession(this, "token");
        String hoten = GECL.getObjectFromSession(this, "hoten");

        txtDate = findViewById(R.id.txtDate);
        txtSothe = findViewById(R.id.txtSothe);
        txtChuthe = findViewById(R.id.txtChuthe);
        txtCCCD = findViewById(R.id.txtCCCD);
        renderSpinner();

        btnDatepicker = findViewById(R.id.txtDatepicker);
        btnDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker(txtDate);
            }
        });
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinnerNganHang = findViewById(R.id.spinnerNganHang);
        spinnerNganHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Loggg",adapterView.getSelectedItem().toString());
                String tennh = adapterView.getSelectedItem().toString();
                for(NganHangJSONObject obj:listNH) {
                    if(tennh.equals(obj.getTennh())) {
                        manh = obj.getManh();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("MANH", manh);
                map.put("MATK", txtSothe.getText().toString());
                map.put("NGAYPHATHANH", txtDate.getText().toString());
                map.put("CCCD", txtCCCD.getText().toString());
                map.put("HOTEN", txtChuthe.getText().toString());
                Call<LinkBankResult> call = RetrofitClient.getRetroInterface().executeLinkBankResult(map);
                call.enqueue(new Callback<LinkBankResult>() {
                    @Override
                    public void onResponse(Call<LinkBankResult> call, Response<LinkBankResult> response) {
                        LinkBankResult rs = response.body();
                        String code = rs.getCode();
                        if(code.equals("e000")) {
                            Intent intent = new Intent(lienketNganHang.this, lienket_nganhang_thanhcong.class);
                            intent.putExtra("hoten", hoten);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                        else {
                            alertDialog(rs.getMessage());
                        }
                    }
                    @Override
                    public void onFailure(Call<LinkBankResult> call, Throwable t) {
                        Log.e("ERRORAPI", t.getMessage());
                        Toast.makeText(lienketNganHang.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void renderSpinner() {
        Call<GetAllBanksResult> call  = RetrofitClient.getRetroInterface().executeGetAllBanksResult();
        call.enqueue(new Callback<GetAllBanksResult>() {
            @Override
            public void onResponse(Call<GetAllBanksResult> call, Response<GetAllBanksResult> response) {
                GetAllBanksResult rs = response.body();
                String code = rs.getCode();
                if(code.equals("e000")) {
                    listNH = rs.getListNH();
                    ArrayList<String> listNHconvertString = new ArrayList<>();
                    listNHconvertString.add("Chọn ngân hàng");
                    for(int i=0;i<listNH.size();i++) {
                        listNHconvertString.add(listNH.get(i).getTennh());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(lienketNganHang.this, R.layout.spinner_nganhang_item,listNHconvertString);
                    adapter.setDropDownViewResource(R.layout.spinner_nganhang_dropdown);
                    spinnerNganHang.setAdapter(adapter);
                }
                else {
                    alertDialog(rs.getMessage());
                }
            }
            @Override
            public void onFailure(Call<GetAllBanksResult> call, Throwable t) {
                Toast.makeText(lienketNganHang.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void DatePicker(TextView setDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String str = i + "-" + (i1+1) + "-" +i2;
                Log.e("Date", str);
                setDate.setText(str);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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