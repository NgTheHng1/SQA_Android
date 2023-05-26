package com.example.mobileappprogrammingproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    Button btnClose;
    EditText txtsdt;
    EditText txtpassword;
    EditText txtname;
    EditText txtCCCD;
    RadioGroup radioGroup;
    RadioButton gender_male;
    RadioButton radioBtnChecked;
    EditText txtemail;
  
     
      

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));
        setContentView(R.layout.thongtincanhan_main);



    }
}