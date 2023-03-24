package com.example.mobileappprogrammingproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class XmplMyAdapter extends RecyclerView.Adapter<XmplMyViewHolder> {

    Context context;
    List<XmplItem> items;

    public XmplMyAdapter(Context context, List<XmplItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public XmplMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new XmplMyViewHolder(LayoutInflater.from(context).inflate(R.layout.xmpl_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull XmplMyViewHolder holder, int position) {
//        holder.txtName.setText(items.get(position).getName());
//        holder.txtEmail.setText(items.get(position).getEmail());
//        holder.imgView.setImageResource(items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
