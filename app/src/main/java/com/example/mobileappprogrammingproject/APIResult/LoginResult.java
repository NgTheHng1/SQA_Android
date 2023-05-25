package com.example.mobileappprogrammingproject.APIResult;

import com.example.mobileappprogrammingproject.ObjectJSON.UserJSONObject;

public class LoginResult {
    private Boolean success;
    private String code;
    private String message;
    private String token;
    private UserJSONObject user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserJSONObject getUser() {
        return user;
    }

    public void setUser(UserJSONObject user) {
        this.user = user;
    }
}
