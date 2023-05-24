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
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillPaidViewAdapter extends RecyclerView.Adapter<BillPaidViewAdapter.BillViewHolder> {

    Context context;
    List<BillTrans> listBillTrans;

    public BillPaidViewAdapter(Context context, List<BillTrans> listBillTrans) {
        this.context = context;
        this.listBillTrans = listBillTrans;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_paid_ele_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        BillTrans thisTrans = listBillTrans.get(position);

        int imgSource = Transactions.getImgSourceDetail()[listBillTrans.get(position).getTransType()];
        holder.ivImage.setImageResource(imgSource);
        holder.tvDetail.setText(thisTrans.getBillName());
        String timeStr = thisTrans.getTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        holder.tvPayDate.setText(timeStr);
        holder.tvAmount.setText(GECL.formatCurrency(thisTrans.getAmount(), "-"));

        if(imgSource != R.drawable.flash_icon){

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.ivImage.getLayoutParams();
            float marginStart = 10;
            float scale = context.getResources().getDisplayMetrics().density;
            int marginStartPx = (int) (marginStart * scale + 0.5f);
            layoutParams.setMarginStart(marginStartPx);
            holder.ivImage.setLayoutParams(layoutParams);
        }

        RelativeLayout itemContainer = (RelativeLayout) holder.ivImage.getParent();
        for (int i = -1; i < itemContainer.getChildCount(); i++) {
            View childView = i != -1 ? itemContainer.getChildAt(i) : itemContainer;
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PaidBillDetailActivity.class);
                    Bundle bundle = new Bundle();

                    String transTime = thisTrans.getTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));

                    bundle.putInt("transType", thisTrans.getTransType());
                    bundle.putInt("amount", thisTrans.getAmount());
                    bundle.putString("time", transTime);
                    bundle.putString("transId", thisTrans.getTransId());
                    bundle.putInt("balanceAfter", thisTrans.getBalanceAfter());
                    bundle.putString("billName", thisTrans.getBillName());
                    bundle.putString("comName", thisTrans.getComName());
                    bundle.putString("isBalanceAfter", "false");

                    intent.putExtra("bundle", bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBillTrans.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvDetail, tvPayDate, tvAmount;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetail = itemView.findViewById(R.id.text_detail);
            tvPayDate = itemView.findViewById(R.id.pay_date);
            tvAmount = itemView.findViewById(R.id.amount);
            ivImage = itemView.findViewById(R.id.image_view);
        }
    }
}
