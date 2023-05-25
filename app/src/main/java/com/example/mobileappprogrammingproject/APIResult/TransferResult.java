package com.example.mobileappprogrammingproject.APIResult;

import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferResult {
    private String success;
    private String code;
    private String message;
    @SerializedName("MaGiaoDich")
    @Expose
    private String MaGD;
    @SerializedName("soDu")
    @Expose
    private String sodu;
    @SerializedName("user")
    @Expose
    private UserJSONObject user;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaGD() {
        return MaGD;
    }

    public void setMaGD(String maGD) {
        MaGD = maGD;
    }

    public String getSodu() {
        return sodu;
    }

    public void setSodu(String sodu) {
        this.sodu = sodu;
    }

    public UserJSONObject getUser() {
        return user;
    }

    public void setUser(UserJSONObject user) {
        this.user = user;
    }
}
