package com.example.mobileappprogrammingproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.Inet4Address;
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

    static public void saveObjectToSession(String value, Context context, String objectName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(objectName, value);
        editor.apply();
    }
    static public String getObjectFromSession(Context context, String objectName){
        return context.getSharedPreferences("session", Context.MODE_PRIVATE).getString(objectName, "");
    }
    static public void moveHomePage(AppCompatActivity activity){
//        context.startActivity(new Intent(activity, HomePageActivity.class));
        activity.startActivity(new Intent(activity, HomePageActivity.class));
        activity.finish();
    }
    static public void setCloseBtnEvent(AppCompatActivity activity, ImageButton imgBtn){
        imgBtn.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity, HomePageActivity.class));
            activity.finish();
        });
    }
    public static void alertDialog(String str, Context context) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(str);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
    }

    public static String clearFormatCurrency(String amount){
        return amount.replaceAll("\\D+", "");
    }

    public static boolean compareAmountBalance(String rawAmount, String balanceStr){
        int amount = Integer.parseInt(rawAmount) * 105 / 100;
        int balance = Integer.parseInt(balanceStr);
        return amount <= balance;
    }

    public static class SuggestItem{
        String activityName;
        int iconResource;

        public SuggestItem(String activityName, int iconResource) {
            this.activityName = activityName;
            this.iconResource = iconResource;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public int getIconResource() {
            return iconResource;
        }

        public void setIconResource(int iconResource) {
            this.iconResource = iconResource;
        }
    }

    public static class MoneyTextWatcher implements TextWatcher {
        private final WeakReference<EditText> editTextWeakReference;

        public MoneyTextWatcher(EditText editText) {
            editTextWeakReference = new WeakReference<EditText>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            try{
                if (editText == null) return;
                String s = editable.toString();
                if (s.isEmpty()) return;
                editText.removeTextChangedListener(this);
                String cleanString = s.replaceAll("\\D+", "");
                DecimalFormat currencyFormatter = new DecimalFormat("#,###");
                String formatted = currencyFormatter.format((double) Integer.parseInt(cleanString));

                editText.setText(formatted);
                editText.setSelection(formatted.length());
                editText.addTextChangedListener(this);
            }catch(NumberFormatException e){
                String str = editable.toString();
                str = str.substring(0,str.length()-1);
                editText.setText(str);
                editText.setSelection(str.length());
                editText.addTextChangedListener(this);
            }
        }
    }
}
// frtd: formatted
