package com.ajinkya.prettymeal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.MessDetailsActivity;
import com.ajinkya.prettymeal.model.MessInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MessCardAdapter extends RecyclerView.Adapter<MessCardAdapter.ViewHolder> {

    private ArrayList<MessInfo> mList;
    private Context context;


    public MessCardAdapter(ArrayList<MessInfo> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<MessInfo> filteredList, Context context){
        this.mList = filteredList;
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MessCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mess_card, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MessCardAdapter.ViewHolder holder, int position) {
        MessInfo messInfo = mList.get(position);
        holder.messName.setText(messInfo.getMessName());
        holder.location.setText(messInfo.getMessLocation());
        holder.price.setText(messInfo.getPrice()+" for one");
        holder.vegMenu.setText(messInfo.getTodayVegMenu());

        //If non-veg Menu is available then show here
        if (!messInfo.getTodayNonVegMenu().equals("NA")) {
            holder.nonVegMenu.setVisibility(View.VISIBLE);
            holder.nonVegMenu.setText(messInfo.getTodayNonVegMenu());
        }


        if (messInfo.getVeg_nonVeg().equals("PureVeg")) {
            holder.veg_nonVegImg.setVisibility(View.GONE);
            holder.vegImg.setVisibility(View.VISIBLE);
        } else if (messInfo.getVeg_nonVeg().equals("NonVeg")) {
            holder.veg_nonVegImg.setVisibility(View.GONE);
            holder.vegImg.setVisibility(View.VISIBLE);
            holder.vegImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_non_veg));
        } else {
            holder.veg_nonVegImg.setVisibility(View.VISIBLE);
            holder.vegImg.setVisibility(View.GONE);
        }

//        holder.bannerImg.setImageDrawable(context.getResources().getDrawable(R.drawable.banner1));

        if (messInfo.getBannerImgURL().isEmpty()){
            holder.bannerImg.setImageDrawable(context.getResources().getDrawable(R.drawable.banner1));
        }else{
            Glide.with(context).load(messInfo.getBannerImgURL()).into(holder.bannerImg);
        }



        holder.messCard.setOnClickListener(View ->{
            Intent intent = new Intent(context, MessDetailsActivity.class);
            intent.putExtra("VegMenu",messInfo.getTodayVegMenu());
            if (messInfo.getTodayNonVegMenu().equals("NA")){
                intent.putExtra("NonVegMenu","");
            }else intent.putExtra("NonVegMenu",messInfo.getTodayNonVegMenu());
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImg, veg_nonVegImg, vegImg;
        TextView messName, location, price, vegMenu, nonVegMenu;
        CardView messCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImg = itemView.findViewById(R.id.bannerImageMC);
            veg_nonVegImg = itemView.findViewById(R.id.vegNonVegImageMC);
            vegImg = itemView.findViewById(R.id.vegImageMC);
            messName = itemView.findViewById(R.id.messNameMC);
            location = itemView.findViewById(R.id.messAddressMC);
            price = itemView.findViewById(R.id.PriceMC);
            vegMenu = itemView.findViewById(R.id.todayVegMenuMC);
            nonVegMenu = itemView.findViewById(R.id.todayNonVegMenuMC);
            messCard = itemView.findViewById(R.id.messCard);
        }
    }
}
