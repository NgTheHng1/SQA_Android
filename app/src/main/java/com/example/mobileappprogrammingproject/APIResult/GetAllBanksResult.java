package com.example.mobileappprogrammingproject.APIResult;
import com.example.mobileappprogrammingproject.ObjectJSON.NganHangJSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllBanksResult {
    private String code;
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<NganHangJSONObject> listNH;

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

    public ArrayList<NganHangJSONObject> getListNH() {
        return listNH;
    }

    public void setListNH(ArrayList<NganHangJSONObject> listNH) {
        this.listNH = listNH;
    }
}
