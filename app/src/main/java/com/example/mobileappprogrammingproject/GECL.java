package com.example.mobileappprogrammingproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Objects;

public class GECL {
    static public String formatCurrency(int value, String prefix){
        NumberFormat formatter = new DecimalFormat("#,###đ");
        return Objects.equals(prefix, "") ? formatter.format(value) : prefix + formatter.format(value);
    }

    static public String formatCurrency(String value, String prefix){
        NumberFormat formatter = new DecimalFormat("#,###đ");
        return Objects.equals(prefix, "") ? formatter.format(Integer.parseInt(value)) : prefix + formatter.format(value);
    }

    static public String toUnaccentedString(String input){
        String unaccentedString = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return unaccentedString.trim().replaceAll("\\s+", " ").toLowerCase();
    }
    static public void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    static public void print(String string){
        Log.e("Error", string);
    }
    static public void makeToast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    static public boolean isKeyInJSON(JSONObject jsonObj, String key){
        try {
            jsonObj.getString(key);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
    static public void saveTokenToSession(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
    static public String getTokenFromSession(Context context){
        return context.getSharedPreferences("session", Context.MODE_PRIVATE).getString("token", "");
    }
}
// frtd: formatted
