package com.ajinkya.prettymeal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;

import java.util.ArrayList;

public class EditMenuAdapter extends RecyclerView.Adapter<EditMenuAdapter.ViewHolder> {

    public ArrayList<String> mList;
    private Context context;


    public EditMenuAdapter(ArrayList<String> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void addItem(String item) {
        mList.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<String> getUpdatedList() {
        return mList;
    }

    @NonNull
    @Override
    public EditMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mess_menu_view, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull EditMenuAdapter.ViewHolder holder, int position) {
        holder.item.setText(mList.get(position));

        holder.DeleteBtn.setOnClickListener(View -> {
            mList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mList.size());
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        ImageView DeleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.MenuItem);
            DeleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
