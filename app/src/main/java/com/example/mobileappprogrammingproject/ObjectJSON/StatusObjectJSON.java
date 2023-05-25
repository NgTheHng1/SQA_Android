package com.example.mobileappprogrammingproject.ObjectJSON;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StatusObjectJSON {
    @SerializedName("type")
    private String type;
    @SerializedName("data")
    private ArrayList<String> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
