package com.example.mobileappprogrammingproject.APIResult;

import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserbyTokenResult {
    private String code;
    private String message;
    @SerializedName("user")
    @Expose
    private UserJSONObject userJSONObject;

    public UserJSONObject getUserJSONObject() {
        return userJSONObject;
    }

    public void setUserJSONObject(UserJSONObject userJSONObject) {
        this.userJSONObject = userJSONObject;
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
}
