package com.example.mobileappprogrammingproject;

import static com.example.mobileappprogrammingproject.GECL.print;
import static com.example.mobileappprogrammingproject.GECL.toUnaccentedString;

//import com.example.mobileappprogrammingproject.GECL.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SuggestItemAdapter extends ArrayAdapter<GECL.SuggestItem> {
    private List<GECL.SuggestItem> listSuggest;
    private List<GECL.SuggestItem> originalList;
    public SuggestItemAdapter(@NonNull Context context, @NonNull List<GECL.SuggestItem> listSuggest) {
        super(context, 0, listSuggest);
        print("1");
        this.listSuggest = listSuggest;
        this.originalList = new ArrayList<>(listSuggest);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        print("2");
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_suggestion, parent, false
            );
        }
        TextView txtActName = view.findViewById(R.id.suggestionTextView);
        ImageView imgIcon = view.findViewById(R.id.iconImageView);

        GECL.SuggestItem item = getItem(position);

        if (item != null) {
            txtActName.setText(item.getActivityName());
            imgIcon.setImageResource(item.getIconResource());
        }

        view.setOnClickListener(view1 -> {
            getContext().startActivity(new Intent(getContext(), Transactions.getActByIcon(item.getIconResource())));
        });

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        print("3");
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            print("4");
            print("size org: " + String.valueOf(originalList.size()));
            print("size list: " + String.valueOf(listSuggest.size()));

            FilterResults results = new FilterResults();
            List<GECL.SuggestItem> suggestions = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                suggestions.addAll(originalList);
            }else{
                String filterPattern = toUnaccentedString(charSequence.toString().toLowerCase().trim());
                for(GECL.SuggestItem item : originalList){
                    String filterActName = toUnaccentedString(item.getActivityName());
                    if(filterActName.contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            print("5");
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            print("6");
            return ((GECL.SuggestItem) resultValue).getActivityName();
        }
    };
}
