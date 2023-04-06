package com.ajinkya.prettymeal.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.adapter.MessMenuAdapter;
import com.ajinkya.prettymeal.model.HistoryModel;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter  extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
    private ArrayList<HistoryModel> mList;
    private Context context;
    private String NameOf;

    public HistoryRecyclerViewAdapter(ArrayList<HistoryModel> mList, Context context, String historyType) {
        this.mList = mList;
        this.context = context;
        if (historyType.equals("Client")){
            NameOf = "Mess Name:  ";
        }else NameOf = "Customer Name:  ";
    }







    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.history_layout, parent, false);
        return new HistoryRecyclerViewAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        HistoryModel historyModel = mList.get(position);

        holder.NameTypeTV.setText(NameOf);
        holder.NameTv.setText(historyModel.getName());
        holder.TransactionNoTv.setText(historyModel.getTransactionNo());
        holder.DateTimeTv.setText(historyModel.getDateTime());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView NameTypeTV, NameTv, TransactionNoTv, DateTimeTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NameTypeTV = itemView.findViewById(R.id.HNameTypeTv);
            NameTv = itemView.findViewById(R.id.HNameTv);
            TransactionNoTv = itemView.findViewById(R.id.HTransactionNoTv);
            DateTimeTv = itemView.findViewById(R.id.HDateTimeTv);
        }
    }
}
