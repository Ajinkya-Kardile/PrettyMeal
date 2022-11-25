package com.ajinkya.prettymeal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.adapter.MessMenuAdapter;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MessDetailsActivity extends AppCompatActivity {
    private ImageSlider imageSlider;
    private RecyclerView vegMenuRecyclerView, nonVegMenuRecyclerView;
    private LinearLayout nonVegMenuLayout;
    ArrayList<String> vegList, nonVegList;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_details);

        Initialize();
    }

    private void Initialize() {
        imageSlider = findViewById(R.id.MessImagesSlider);
        vegMenuRecyclerView = findViewById(R.id.RecyclerViewVegFoodMenu);
        nonVegMenuRecyclerView = findViewById(R.id.RecyclerViewNonVegFoodMenu);
        nonVegMenuLayout = findViewById(R.id.NonVegMenu);
        backButton = findViewById(R.id.profile_image);

        backButton.setOnClickListener(View->{
            onBackPressed();
        });


        Bundle bundle = getIntent().getExtras();
        String vegMenu = bundle.getString("VegMenu");
        String nonVegMenu = bundle.getString("NonVegMenu");
//        String MessName = bundle.getString("NonVegMenu");
//        String MessDetails = bundle.getString("NonVegMenu");
//        String MessPhoneNo = bundle.getString("NonVegMenu");
//        String MessEmail = bundle.getString("NonVegMenu");
//        String VegNonVegType = bundle.getString("NonVegMenu");
//        String MessOwnerName = bundle.getString("NonVegMenu");


        vegList = new ArrayList<>();
        nonVegList = new ArrayList<>();




        List<SlideModel> ImagesList = new ArrayList<>();
//        ImagesList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://img.freepik.com/premium-photo/indian-hindu-veg-thali-also-known-as-food-platter-is-complete-lunch-dinner-meal-closeup-selective-focus_466689-9116.jpg?w=2000", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://i.pinimg.com/736x/0f/13/7d/0f137d2a243f7b63e5716ab4c10c3ee3.jpg", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://spicytamarind.com.au/wp-content/uploads/2021/01/south-indian-thali-2.jpg?ezimgfmt=rs:371x371/rscb6/ngcb6/notWebP", ScaleTypes.FIT));
        imageSlider.setImageList(ImagesList, ScaleTypes.FIT);



        vegMenuRecyclerView.setHasFixedSize(true);
        vegMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!vegMenu.isEmpty()){
            String[] strSplit = vegMenu.split(",");
            vegList = new ArrayList<>(Arrays.asList(strSplit));
            MessMenuAdapter messVegMenuAdapter = new MessMenuAdapter(vegList);
            vegMenuRecyclerView.setAdapter(messVegMenuAdapter);
        }


        nonVegMenuRecyclerView.setHasFixedSize(true);
        nonVegMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!nonVegMenu.isEmpty()){
            String[] strSplit = nonVegMenu.split(",");
            nonVegList = new ArrayList<>(Arrays.asList(strSplit));
            MessMenuAdapter messNonVegMenuAdapter = new MessMenuAdapter(nonVegList);
            nonVegMenuRecyclerView.setAdapter(messNonVegMenuAdapter);
        }else {
            nonVegMenuLayout.setVisibility(View.GONE);
        }


    }
}