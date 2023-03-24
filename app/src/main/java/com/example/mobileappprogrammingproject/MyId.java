package com.example.mobileappprogrammingproject;

public class MyId {
    public static String getAdd(String route){
        return String.format("http://%s:3000/%s","192.168.1.9", route);
    }
}
