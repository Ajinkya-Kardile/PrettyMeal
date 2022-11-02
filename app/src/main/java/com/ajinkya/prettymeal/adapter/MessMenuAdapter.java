package com.ajinkya.prettymeal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;

import java.util.ArrayList;

public class MessMenuAdapter extends RecyclerView.Adapter<MessMenuAdapter.ViewHolder> {

    private ArrayList<String> mList;

    public MessMenuAdapter(ArrayList<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MessMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.food_menu_item, parent, false);
        return new MessMenuAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MessMenuAdapter.ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.foodMenuTV);
        }
    }
}
