package com.ajinkya.prettymeal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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
        ImagesList.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAf6VMgX30jHN9Lb7SFdxgqdyCwYDOK6eHpg&usqp=CAU", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://i.pinimg.com/736x/da/66/24/da66249a283dafab8488b5c3bddf56f1.jpg", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://i.ytimg.com/vi/mnCDSmooRxA/maxresdefault.jpg", ScaleTypes.FIT));
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