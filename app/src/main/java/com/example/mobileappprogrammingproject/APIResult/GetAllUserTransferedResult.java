package com.example.mobileappprogrammingproject.APIResult;
import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllUserTransferedResult {
    private String success;
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<UserJSONObject> listUser;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<UserJSONObject> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<UserJSONObject> listUser) {
        this.listUser = listUser;
    }
}
