package com.example.mobileappprogrammingproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.thongtincanhan_main);



    }
}