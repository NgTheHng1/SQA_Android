package com.example.mobileappprogrammingproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListBankViewAdapter extends RecyclerView.Adapter<ListBankViewAdapter.BankItemViewHolder>{
    Context context;
    List<Bank> listBank;
    int checkedPosition = -1;

    public ListBankViewAdapter(Context context, List<Bank> listBank) {
        this.context = context;
        this.listBank = listBank;
    }

    @NonNull
    @Override
    public BankItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BankItemViewHolder(LayoutInflater.from(context).inflate(R.layout.bank_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankItemViewHolder holder, int position) {
//        holder.txtMonth.setText(String.format("Tháng %s", listHisTrans.get(position).get(0).getTime().format(DateTimeFormatter.ofPattern("MM/yyyy"))));
        RecyclerView recy = (RecyclerView) ((Activity) context).findViewById(R.id.recy_list_bank);

        holder.imgBank.setImageResource(listBank.get(position).getSourceImg());
        holder.tvBankName.setText(listBank.get(position).getBankName());
        holder.tvBankFee.setText("Miễn phí");
        holder.cbBankSelect.setChecked(false);
        holder.itemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.unchecked_bank_item_color));

        holder.cbBankSelect.setChecked(checkedPosition == position);

        holder.cbBankSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && checkedPosition != holder.getBindingAdapterPosition()) {
                    int lastChecked = checkedPosition;
                    checkedPosition = holder.getBindingAdapterPosition();

                    holder.itemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

                    notifyItemChanged(lastChecked);
                }else{
                    holder.itemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.unchecked_bank_item_color));
                    if(checkedPosition == holder.getBindingAdapterPosition())
                        checkedPosition = -1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBank.size();
    }


    public static class BankItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgBank;
        TextView tvBankName, tvBankFee;
        CheckBox cbBankSelect;
        RelativeLayout itemContainer;
        public BankItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBank = itemView.findViewById(R.id.bank_logo);
            tvBankName = itemView.findViewById(R.id.text_bank_name);
            tvBankFee = itemView.findViewById(R.id.bank_fee);
            cbBankSelect = itemView.findViewById(R.id.bank_check_box);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
