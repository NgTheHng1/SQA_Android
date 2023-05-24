package com.example.mobileappprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillViewAdapter extends RecyclerView.Adapter<BillViewAdapter.BillViewHolder> {

    Context context;
    List<BillTrans> listBillTrans;

    public BillViewAdapter(Context context, List<BillTrans> listBillTrans) {
        this.context = context;
        this.listBillTrans = listBillTrans;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_ele_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        BillTrans thisBill = listBillTrans.get(position);

        int imgSource = Transactions.getImgSourceDetail()[thisBill.getTransType()];
        holder.ivImage.setImageResource(imgSource);
        if(imgSource != R.drawable.flash_icon){
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.ivImage.getLayoutParams();
            float marginStart = 10;
            float scale = context.getResources().getDisplayMetrics().density;
            int marginStartPx = (int) (marginStart * scale + 0.5f);
            layoutParams.setMarginStart(marginStartPx);
            holder.ivImage.setLayoutParams(layoutParams);
        }
        holder.tvDetail.setText(thisBill.getBillName());
        String timeStr = thisBill.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        holder.tvDueDate.setText(timeStr);
        holder.tvAmount.setText(GECL.formatCurrency(thisBill.getAmount(), ""));

        RelativeLayout itemContainer = (RelativeLayout) holder.ivImage.getParent();
        for (int i = -1; i < itemContainer.getChildCount(); i++) {
            View childView = i != -1 ? itemContainer.getChildAt(i) : itemContainer;
            childView.setOnClickListener(view -> {
                Intent intent = new Intent(context, PayBillConfirmActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("comName", thisBill.getComName());
                bundle.putString("cusId", thisBill.getCusId());
                bundle.putString("cusName", thisBill.getCusName());
                bundle.putString("address", thisBill.getAddress());
                bundle.putString("billName", thisBill.getBillName());
                bundle.putString("amount", String.valueOf(thisBill.getAmount()));
                bundle.putString("transId", thisBill.getTransId());

                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBillTrans.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvDetail, tvDueDate, tvAmount;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvDetail = itemView.findViewById(R.id.text_detail);
            this.tvDueDate = itemView.findViewById(R.id.due_date);
            tvAmount = itemView.findViewById(R.id.amount);
            ivImage = itemView.findViewById(R.id.image_view);
        }
    }
}
