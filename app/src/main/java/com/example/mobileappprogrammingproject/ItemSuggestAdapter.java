package com.example.mobileappprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ItemSuggestAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] suggestions;
    public ItemSuggestAdapter(@NonNull Context context, String[] suggestions) {
        super(context, R.layout.item_suggestion, suggestions);
        this.context = context;
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Class classIntentTo = (Class) getSuggestInfo(position)[0];
        int drawableSource = (int) getSuggestInfo(position)[1];

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        ImageView iconImageView = view.findViewById(R.id.iconImageView);
        TextView suggestionTextView = view.findViewById(R.id.suggestionTextView);

        String suggestion = suggestions[position];

        suggestionTextView.setText(suggestion);
        iconImageView.setImageResource(drawableSource);

        view.setOnClickListener(view1 -> {
            context.startActivity(new Intent(context, classIntentTo));
        });

        return view;
    }
    private Object[] getSuggestInfo(int postion){
        Class<? extends AppCompatActivity>[] classList = new Class[]{
                WithdrawActiveActivity.class,
                WithdrawActiveActivity.class,
                WithdrawActiveActivity.class,
                PayBillViewActivity.class
        };
        int[] iconSourceList = new int[]{
                R.drawable.exchange_arrow,
                R.drawable.add_money,
                R.drawable.withdraw_money,
                R.drawable.pay_bill
        };
        return new Object[]{classList[postion], iconSourceList[postion]};
    }
}
