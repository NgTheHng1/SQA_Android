package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.hideKeyboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.GetCardBankbyUserResult;
import com.example.mobileappprogrammingproject.ObjectJSON.TaiKhoanNganHangJSONObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class naptien_trangchu extends AppCompatActivity {
    ListView listNganhang;
    EditText txtSotien;
    Button btnNaptien, btnClose, btnThemNH;
    TextView txtSodu;
    ArrayList<TaiKhoanNganHangJSONObject> listNHfromAPI = new ArrayList<>();
    String matk, token, hoten, sdt, sodu, tennh;
    NganhangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.naptien_trangchu);  Intent intent = getIntent();
        token = GECL.getTokenFromSession(this);
        hoten = GECL.getObjectFromSession(this, "hoten");
        sdt = GECL.getObjectFromSession(this, "sdt");
//        sodu = GECL.getObjectFromSession(this, "sodu");

        setControl();
        setEvent();
        Promise.resolve()
                //get balance of user
                .then((action, data) -> {
                    Call<String> call = RetrofitClient.getRetroInterface().getBalanceByUser(token);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseStr = response.body();
                            GECL.print(responseStr);
                            try {
                                JSONObject jsonObj = new JSONObject(responseStr);
                                sodu = jsonObj.getJSONObject("balance").getString("SoDu");
                                txtSodu.setText(GECL.formatCurrency(sodu, ""));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            action.resolve();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            GECL.makeToast("Lỗi", naptien_trangchu.this);
                            action.reject();
                        }
                    });
                })
                .then((action, data) -> {
                    renderListNganHangFromAPI(token, sdt);
                })
                .start();
    }

    private void renderView() {
        txtSodu.setText(sodu);
    }

    private void setEvent() {
        txtSotien.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                hideKeyboard(this);
                txtSotien.clearFocus();
                return true;
            }
            return false;
        });
        txtSotien.addTextChangedListener(new GECL.MoneyTextWatcher(txtSotien));
//        listNganhang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("Baohan", listNHfromAPI.get(i).getTennh());
//                posNganhang = listNHfromAPI.get(i).getTennh();
//                for(TaiKhoanNganHangJSONObject tknh : listNHfromAPI) {
//                    if(tknh.getTennh().equals(listNHfromAPI.get(i).getTennh())) {
//                        matk = tknh.getMatk();
//                        tennh = tknh.getTennh();
//                    }
//                }
//            }
//        });
        btnNaptien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelectedInfo()[2].equals("-1")) {
                    alertDialog("Bạn vui lòng chọn ngân hàng để nạp tiền!");
                    return;
                }
               String matk = "";
               matk = adapter.getSelectedInfo()[0];
               tennh = adapter.getSelectedInfo()[1];
                if(checkEmpty(txtSotien)) {
                    txtSotien.setError("Vui lòng nhập số tiền.");
                }
                else if (matk.equals("")) {
                    alertDialog("Vui lòng chọn Ngân Hàng.");
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("sotien", GECL.clearFormatCurrency(txtSotien.getText().toString()));
                    bundle.putString("ten_nghang", tennh);
                    bundle.putString("sotk", matk);
                    startActivity(new Intent(naptien_trangchu.this, naptien_confirm.class)
                            .putExtra("bundle", bundle));
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnThemNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(naptien_trangchu.this, lienketNganHang.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        txtSodu = findViewById(R.id.txtSodu);
        listNganhang = findViewById(R.id.listNganhang);
        txtSotien = findViewById(R.id.txtSotien);
        btnNaptien = findViewById(R.id.btnNaptien);
        btnClose = findViewById(R.id.btnClose);
        btnThemNH = findViewById(R.id.btnThemnganhang);
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
                        adapter = new NganhangAdapter(naptien_trangchu.this, R.layout.nganhang_item, listNHfromAPI);
                        listNganhang.setAdapter(adapter);
                        Log.e("Ngan Hang" ,adapter.getSelectedInfo()[0]);
                    }
                    else {
                        String []list = {"Chưa có tài khoản ngân hàng nào"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(naptien_trangchu.this, android.R.layout.simple_list_item_1, list);
                        listNganhang.setAdapter(adapter);
                    }

                }
                else {
                    alertDialog(rs.getMessage());
                }
            }
            @Override
            public void onFailure(Call<GetCardBankbyUserResult> call, Throwable t) {
                Toast.makeText(naptien_trangchu.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public Boolean checkEmpty(EditText editText) {
        if(editText.getText().toString().equals("")) {
            return true;
        }
        return false;
    }
}