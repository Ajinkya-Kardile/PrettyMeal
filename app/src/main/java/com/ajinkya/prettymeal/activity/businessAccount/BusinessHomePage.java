package com.ajinkya.prettymeal.activity.businessAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessHomePage extends AppCompatActivity {


    private TextView MessNameTV, MessLocationTV, NonVegMenuHeading;
    private ImageView bannerImg, veg_nonVegImgBanner, vegImgBanner;
    private TextView bannerMessName, BannerLocation, bannerPrice, BannerVegMenu, BannerNonVegMenu;
    private Button EditMenu;
    private RecyclerView vegMenuRecyclerview, NonVegMenuRecyclerView;
    private CardView messCard, Wallet, profile, EditMessInfo,History, AboutUs, Support;
    private CircleImageView profileImage;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private String UserUid;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home_page);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);

//        String menuString =" adfsf+ sdf+fdsfa+ sdf+fsfsfdf";
//        String[] tokens = menuString.split("\\+");

        Initialize();



    }

    private void Initialize() {
        // TextView initialize
        MessNameTV = findViewById(R.id.MessNameHomePage);
        MessLocationTV = findViewById(R.id.LocationTextViewBHP);
        bannerMessName = findViewById(R.id.messNameMC);
        BannerLocation = findViewById(R.id.messAddressMC);
        BannerVegMenu = findViewById(R.id.todayVegMenuMC);
        BannerNonVegMenu = findViewById(R.id.todayNonVegMenuMC);
        bannerPrice = findViewById(R.id.PriceMC);
        NonVegMenuHeading = findViewById(R.id.NonVegMenuHeading);

//        Other View initialize
        bannerImg = findViewById(R.id.bannerImageMC);
        vegImgBanner = findViewById(R.id.vegImageMC);
        veg_nonVegImgBanner = findViewById(R.id.vegNonVegImageMC);
        EditMenu = findViewById(R.id.EditMenuBtn);
        vegMenuRecyclerview = findViewById(R.id.VegMenuRecyclerView);
        NonVegMenuRecyclerView = findViewById(R.id.NonVegMenuRecyclerView);

        messCard = findViewById(R.id.messCard);
        Wallet = findViewById(R.id.WalletIcon);
        profile = findViewById(R.id.ProfileIcon);
        EditMessInfo = findViewById(R.id.EditMessIcon);
        History = findViewById(R.id.HistoryIcon);
        AboutUs = findViewById(R.id.AboutUsIcon);
        Support = findViewById(R.id.SupportIcon);
        profileImage = findViewById(R.id.profile_imageBHP);


    }
}