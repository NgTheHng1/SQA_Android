package com.example.mobileappprogrammingproject.ObjectJSON;

import com.google.gson.annotations.SerializedName;

public class NganHangJSONObject {
    @SerializedName("MANH")

    private String manh;
    @SerializedName("TENNH")

    private String tennh;
    @SerializedName("CHIETKHAU")

    private String chietkhau;

    public String getManh() {
        return manh;
    }

    public void setManh(String manh) {
        this.manh = manh;
    }

    public String getTennh() {
        return tennh;
    }

    public void setTennh(String tennh) {
        this.tennh = tennh;
    }

    public String getChietkhau() {
        return chietkhau;
    }

    public void setChietkhau(String chietkhau) {
        this.chietkhau = chietkhau;
    }
}
