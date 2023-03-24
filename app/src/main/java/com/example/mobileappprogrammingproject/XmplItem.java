package com.example.mobileappprogrammingproject;

public class XmplItem {
    String name, email;
    int image;

    public XmplItem(String name, String email, int image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public XmplItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
