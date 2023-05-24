package com.example.mobileappprogrammingproject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

public class LoadingDialog {
    private Activity activity;
    private androidx.appcompat.app.AlertDialog dialog;

    LoadingDialog(Activity activity){
        this.activity = activity;
    }

    void loadingAlertDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}
