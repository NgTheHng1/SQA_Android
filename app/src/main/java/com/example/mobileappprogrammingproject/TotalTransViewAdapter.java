package com.example.mobileappprogrammingproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TotalTransViewAdapter extends RecyclerView.Adapter<TotalTransViewAdapter.HisTransTotalViewHolder>{
    Context context;
    List<List<Transactions>> listHisTrans;

    public TotalTransViewAdapter(Context context, List<List<Transactions>> listHisTrans) {
        this.context = context;
        this.listHisTrans = listHisTrans;
    }

    @NonNull
    @Override
    public HisTransTotalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HisTransTotalViewHolder(LayoutInflater.from(context).inflate(R.layout.total_trans_his_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HisTransTotalViewHolder holder, int position) {
        holder.txtMonth.setText(String.format("Tháng %s", listHisTrans.get(position).get(0).getTime().format(DateTimeFormatter.ofPattern("MM/yyyy"))));

        holder.recyTotalHisTrans.setLayoutManager(new LinearLayoutManager(context));
        holder.recyTotalHisTrans.setAdapter(new EleTransViewAdapter(context, listHisTrans.get(position)));
    }

    @Override
    public int getItemCount() {
        return listHisTrans.size();
    }


    public static class HisTransTotalViewHolder extends RecyclerView.ViewHolder{
        TextView txtMonth;
        RecyclerView recyTotalHisTrans;
        public HisTransTotalViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.month_text);
            recyTotalHisTrans = itemView.findViewById(R.id.recy_trans_item_by_month);
        }
    }
}
