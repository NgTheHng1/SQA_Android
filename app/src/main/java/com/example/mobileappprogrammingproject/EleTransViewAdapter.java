package com.example.mobileappprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class EleTransViewAdapter extends RecyclerView.Adapter<EleTransViewAdapter.HisTransEleViewHolder>{
    Context context;
    List<Transactions> listHisTrans;

    public EleTransViewAdapter(Context context, List<Transactions> listHisTrans) {
        this.context = context;
        this.listHisTrans = listHisTrans;
    }

    @NonNull
    @Override
    public HisTransEleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HisTransEleViewHolder(LayoutInflater.from(context).inflate(R.layout.ele_trans_his_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HisTransEleViewHolder holder, int position) {
        String[] listTransType = Transactions.getListTransTypeStr();
        int[] listImgSource = Transactions.getImgSource();

        Transactions thisTrans = listHisTrans.get(position);

        int transType = thisTrans.getTransType();
        String receiverOrSender = thisTrans.getReceiverOrSender();
        String amountPrefix = thisTrans.getPrefixOfAmount();

        int imageSource = listImgSource[transType];
        String transTypeStr = listTransType[transType] + " " + receiverOrSender;
        String transTime = thisTrans.getTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        String balanceAfter = formatCurrency(thisTrans.getBalanceAfter(), "");
        String amount = formatCurrency(thisTrans.getAmount(), amountPrefix);

        holder.ivTransImg.setImageResource(imageSource);
        holder.tvTransType.setText(transTypeStr);
        holder.tvTransTime.setText(transTime);
        holder.tvBalanceAfter.setText(balanceAfter);
        holder.tvAmount.setText(amount);
        if(position % 2 == 1){
            int color = ContextCompat.getColor(context, R.color.second_trans_item_color);
            ((RelativeLayout)holder.ivTransImg.getParent()).setBackgroundColor(color);
        }

        //Set content and event of each item
        RelativeLayout itemContainer = (RelativeLayout) holder.ivTransImg.getParent();
        for (int i = -1; i < itemContainer.getChildCount(); i++) {

            View childView = i != -1 ? itemContainer.getChildAt(i) : itemContainer;
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String brand = "Ví MiMi";
                    Class thisClass = thisTrans.getActivityIntentTo();
                    Intent intent = new Intent(context, thisClass);
                    Bundle bundle = new Bundle();

                    bundle.putString("transType", Transactions.getListSimpleTransTypeStr()[transType]);
                    bundle.putString("amount", amount);
                    bundle.putString("transTime", transTime);
                    bundle.putString("transId",thisTrans.getTransId());
                    bundle.putString("fee", "Miễn phí");
                    bundle.putString("balanceAfter", balanceAfter);

                    if(Objects.equals(thisClass, TransferReceiveTransDetailActivity.class)){
                        bundle.putString("sender", ((TransferTrans) thisTrans).getOppoPerson().getUser().getFullName());
                        bundle.putString("receiver", brand);
                        bundle.putString("phoneNum", ((TransferTrans) thisTrans).getOppoPerson().getPhoneNum());
                        bundle.putString("message", ((TransferTrans) thisTrans).getMessage());
                    }
                    else if(Objects.equals(thisClass, TransferTransDetailActivity.class)){
                        bundle.putString("sender", brand);
                        bundle.putString("receiver", ((TransferTrans) thisTrans).getOppoPerson().getUser().getFullName());
                        bundle.putString("phoneNum", ((TransferTrans) thisTrans).getOppoPerson().getPhoneNum());
                        bundle.putString("message", ((TransferTrans) thisTrans).getMessage());
                    }
                    else if(Objects.equals(thisClass, DepositTransDetailActivity.class)){
                        bundle.putString("sender", ((BankTrans) thisTrans).getBankAccount().getBank().getBankName());
                        bundle.putString("receiver", brand);
                    }
                    else if(Objects.equals(thisClass, WithdrawTransDetailActivity.class)){
                        bundle.putString("sender", brand);
                        bundle.putString("receiver", ((BankTrans) thisTrans).getBankAccount().getBank().getBankName());
                    }

                    intent.putExtra("bundlePackage", bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
    private String formatCurrency(int value, String prefix){
        NumberFormat formatter = new DecimalFormat("#,###đ");
        return Objects.equals(prefix, "") ? formatter.format(value) : prefix + formatter.format(value);
    }

    @Override
    public int getItemCount() {
        return listHisTrans.size();
    }

    public static class HisTransEleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTransImg;
        TextView tvTransType, tvTransTime, tvBalanceAfter, tvAmount;
        public HisTransEleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTransImg = itemView.findViewById(R.id.trans_image);
            tvTransType = itemView.findViewById(R.id.trans_type);
            tvTransTime = itemView.findViewById(R.id.trans_detail_time);
            tvBalanceAfter = itemView.findViewById(R.id.balance_number);
            tvAmount = itemView.findViewById(R.id.blance_change);
        }
    }
}
