package com.example.mobileappprogrammingproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class XmplMyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgView;
    TextView txtName, txtEmail;
    public XmplMyViewHolder(@NonNull View itemView) {
        super(itemView);
        setControl(itemView);
    }
    private void setControl(View itemView){
        imgView = itemView.findViewById(R.id.imageView);
        txtName = itemView.findViewById(R.id.name);
        txtEmail = itemView.findViewById(R.id.email);
    }
}
