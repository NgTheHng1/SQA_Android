package com.example.mobileappprogrammingproject.APIResult;

import com.example.mobileappprogrammingproject.ObjectJSON.TaiKhoanNganHangJSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCardBankbyUserResult {
    private String code;
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<TaiKhoanNganHangJSONObject> listTKNH;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TaiKhoanNganHangJSONObject> getListTKNH() {
        return listTKNH;
    }

    public void setListTKNH(ArrayList<TaiKhoanNganHangJSONObject> listTKNH) {
        this.listTKNH = listTKNH;
    }
}
