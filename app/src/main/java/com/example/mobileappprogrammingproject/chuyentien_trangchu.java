package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.hideKeyboard;
import static com.example.mobileappprogrammingproject.GECL.print;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobileappprogrammingproject.APIResult.GetAllUserTransferedResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.promise.Promise;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chuyentien_trangchu extends AppCompatActivity {
    ListView listUserTransfered;
    Button btnClose, btnAccept;
    EditText txtSdt;
    ArrayList<UserJSONObject> listUser = new ArrayList<>();
    private String posUserHoten = "";
    private String posUserSdt = "";
    boolean isListUserClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.chuyentien_trangchu);  Intent intent = getIntent();
        String token = GECL.getTokenFromSession(this);
        String adminHoten = GECL.getObjectFromSession(this, "hoten");
        String adminSDT = GECL.getObjectFromSession(this, "sdt");
        renderListUserTransfered(token);
        txtSdt = findViewById(R.id.txtSdt);
        listUserTransfered = findViewById(R.id.listUserTransfered);
        listUserTransfered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posUserSdt = listUser.get(i).getSdt();
                posUserHoten = listUser.get(i).getHoten();
                txtSdt.setText(posUserSdt);
            }
        });

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSdt.getText().toString().equals("")){
                    alertDialog("Vui lòng nhập SDT người nhận");
                    return;
                }

                if(txtSdt.getText().toString().length() != 10){
                    alertDialog("Vui lòng nhập SDT hợp lệ!");
                    return;
                }
                print("message");
                Promise.resolve()
                        .then((action, data) -> {
                            Call<String> call = null;
                            try{
                             call = RetrofitClient.getRetroInterface().getUsernamByPhoneNum(txtSdt.getText().toString());
                            }catch(Exception e){
                                print(e.getMessage());
                            }
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    try {
                                        String userName = (new JSONObject(response.body())).getString("userName");
                                        if(userName.equals("")) {
                                            alertDialog("Số điện thoại không tồn tại trong hệ thống");
                                            action.reject();
                                            return;
                                        }
                                        action.resolve(userName);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    print(t.getMessage());
                                }
                            });
                        })
                        .then((action, data) -> {
                            String userName = (String) data;
                            Intent intent = new Intent(chuyentien_trangchu.this, chuyentien_sotien.class);
                            intent.putExtra("token", token);
                            intent.putExtra("userHoten", userName);
                            intent.putExtra("userSdt", txtSdt.getText().toString());
                            intent.putExtra("adminHoten", adminHoten);
                            intent.putExtra("adminSDT", adminSDT);
                            startActivity(intent);
                            finish();
                        })
                        .start();
            }
        });
        txtSdt.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                hideKeyboard(this);
                txtSdt.clearFocus();
                return true;
            }
            return false;
        });
    }

    public void renderListUserTransfered(String token) {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        ArrayList<String> listUserAdapter = new ArrayList<>();
        Call<GetAllUserTransferedResult> call  = RetrofitClient.getRetroInterface().executeGetAllUserTransferedResult(map);
        call.enqueue(new Callback<GetAllUserTransferedResult>() {
            @Override
            public void onResponse(Call<GetAllUserTransferedResult> call, Response<GetAllUserTransferedResult> response) {
                GetAllUserTransferedResult rs = response.body();
                String code = rs.getCode();
                listUser = rs.getListUser();
                if(code.equals("e000")) {
                    if(listUser.size() != 0) {
                        for(UserJSONObject user : listUser) {
//                        Log.e("TenNH", tknh.getTennh());
                            listUserAdapter.add(user.getSdt() + " - " + user.getHoten());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(chuyentien_trangchu.this, android.R.layout.simple_list_item_1, listUserAdapter);
                        listUserTransfered.setAdapter(adapter);
                    }
                    else {
                        String []list = {"Chưa có lịch sử chuyển tiền"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(chuyentien_trangchu.this, android.R.layout.simple_list_item_1, list);
                        listUserTransfered.setAdapter(adapter);
                        listUserTransfered.setEnabled(false);
                    }
                }
                else {
                    alertDialog("Lỗi render");
                }
            }
            @Override
            public void onFailure(Call<GetAllUserTransferedResult> call, Throwable t) {
                Toast.makeText(chuyentien_trangchu.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}