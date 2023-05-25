package com.example.mobileappprogrammingproject.ObjectJSON;

import com.google.gson.annotations.SerializedName;

public class UserJSONObject {
    @SerializedName("SDT")
    private String sdt;
    @SerializedName("Mail")
    private String mail;
    @SerializedName("HoTen")
    private String hoten;
    @SerializedName("GioiTinh")
    private String gioitinh;
    @SerializedName("SoDu")
    private String sodu;
    @SerializedName("CCCD")
    private String cccd;


    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getSodu() {
        return sodu;
    }

    public void setSodu(String sodu) {
        this.sodu = sodu;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}
