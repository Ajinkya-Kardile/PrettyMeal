package com.ajinkya.prettymeal.activity.businessAccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;

import java.util.ArrayList;

public class BusinessHomePageVegMenuAdapter extends RecyclerView.Adapter<BusinessHomePageVegMenuAdapter.ViewHolder> {
    public String[] mList;
    private Context context;


    public BusinessHomePageVegMenuAdapter(String[] mList, Context context) {
        this.mList = mList;
        this.context = context;
    }


    @NonNull
    @Override
    public BusinessHomePageVegMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.food_menu_item, parent, false);
        return new BusinessHomePageVegMenuAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHomePageVegMenuAdapter.ViewHolder holder, int position) {
        holder.item.setText(mList[position]);
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.foodMenuTV);
        }
    }
}
