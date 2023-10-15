package com.ajinkya.prettymeal.activity.businessAccount;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.AboutUsPage;
import com.ajinkya.prettymeal.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessHomePage extends AppCompatActivity {


    private TextView MessNameTV, MessLocationTV, NonVegMenuHeading;
    private ImageView bannerImg, veg_nonVegImgBanner, vegImgBanner;
    private TextView bannerMessName, BannerLocation, bannerPrice, BannerVegMenu, BannerNonVegMenu;
    private Button EditMenu;
    private RecyclerView vegMenuRecyclerview, NonVegMenuRecyclerView;
    private CardView messCard, Wallet, profile, EditMessInfo, History, AboutUs, Support;
    private CircleImageView profileImage;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference UserInfoReference, MessInfoReference;
    private String UserUid, UserName, UserEmail, UserPhoneNo, Membership, ProfileImageURL;
    private String MessName, MessShortAddress, MessFullAddress, SupportEmail, SupportPhoneNo, Latitude, Longitude, MessDetails, MessType, VegMenu, NonVegMenu, Price;
    private UserInfo userInfo;
    private BusinessHomePageVegMenuAdapter vegMenuAdapter, NonVegMenuAdapter;
    private String[] vegMenuList, nonVegMenuList;

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
        FatchData();
        Buttons();


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


        //firebase ==>
        String Current_Uid = FirebaseAuth.getInstance().getUid();
        Log.e(TAG, "Initialize: Current_Uid: " + Current_Uid);
        assert Current_Uid != null;
        UserInfoReference = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("UserInfo");
        MessInfoReference = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("MessDetails");


//        Adapter==>


    }

    private void FatchData() {
        UserInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "onDataChange: UserInfo " + snapshot);

                UserName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                UserEmail = Objects.requireNonNull(snapshot.child("Email").getValue()).toString();
                UserPhoneNo = Objects.requireNonNull(snapshot.child("PhoneNo").getValue()).toString();
                ProfileImageURL = Objects.requireNonNull(snapshot.child("ProfileImg").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MessInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "onDataChange: MessInfo " + snapshot);

                MessName = Objects.requireNonNull(snapshot.child("MessName").getValue()).toString();
                SupportEmail = Objects.requireNonNull(snapshot.child("SupportEmail").getValue()).toString();
                SupportPhoneNo = Objects.requireNonNull(snapshot.child("SupportPhoneNo").getValue()).toString();
                MessType = Objects.requireNonNull(snapshot.child("MessType").getValue()).toString();
                MessDetails = Objects.requireNonNull(snapshot.child("MessDesc").getValue()).toString();
                Latitude = Objects.requireNonNull(snapshot.child("Latitude").getValue()).toString();
                Longitude = Objects.requireNonNull(snapshot.child("Longitude").getValue()).toString();
                MessShortAddress = Objects.requireNonNull(snapshot.child("ShortAddress").getValue()).toString();
                MessFullAddress = Objects.requireNonNull(snapshot.child("FullAddress").getValue()).toString();
                VegMenu = Objects.requireNonNull(snapshot.child("VegMenu").getValue()).toString();
                NonVegMenu = Objects.requireNonNull(snapshot.child("NonVegMenu").getValue()).toString();
                Price = Objects.requireNonNull(snapshot.child("Price").getValue()).toString();
                SetDataToViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void SetDataToViews() {
        MessNameTV.setText(MessName);
        MessLocationTV.setText(MessShortAddress);
        bannerMessName.setText(MessName);
        BannerLocation.setText(MessFullAddress);
        bannerPrice.setText("Rs " + Price + " For Each");

        if (MessType.equals("PureVeg")) {
            vegImgBanner.setVisibility(View.VISIBLE);
            veg_nonVegImgBanner.setVisibility(View.GONE);
        }


        BannerVegMenu.setText(VegMenu);
        vegMenuList = VegMenu.split("\\+");

        vegMenuAdapter = new BusinessHomePageVegMenuAdapter(vegMenuList, this);
        vegMenuRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vegMenuRecyclerview.setAdapter(vegMenuAdapter);


        if (!MessType.equals("PureVeg")) {
            BannerNonVegMenu.setText(NonVegMenu);
            BannerNonVegMenu.setVisibility(View.VISIBLE);

            NonVegMenuHeading.setVisibility(View.VISIBLE);
            NonVegMenuRecyclerView.setVisibility(View.VISIBLE);


            nonVegMenuList = NonVegMenu.split("\\+");

            NonVegMenuAdapter = new BusinessHomePageVegMenuAdapter(nonVegMenuList, this);
            NonVegMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            NonVegMenuRecyclerView.setAdapter(NonVegMenuAdapter);

        }


    }

    private void Buttons() {
        Wallet.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessHomePage.this, BusinessWalletActivity.class);
            startActivity(intent);
        });

        EditMenu.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessHomePage.this, EditMessMenuActivity.class);
            intent.putExtra("MessType", MessType);
            startActivity(intent);
        });

        profile.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessHomePage.this, BusinessProfilePage.class);
            intent.putExtra("UserName", UserName);
            intent.putExtra("UserEmail", UserEmail);
            intent.putExtra("UserMobileNo", UserPhoneNo);
            startActivity(intent);
        });

        EditMessInfo.setOnClickListener(View -> {

            Intent intent = new Intent(BusinessHomePage.this, EditMessInfo.class);
            intent.putExtra("MessName", MessName);
            intent.putExtra("SupportMobileNo", SupportPhoneNo);
            intent.putExtra("SupportEmail", SupportEmail);
            intent.putExtra("MessType", MessType);
            intent.putExtra("MessDescription", MessDetails);
            intent.putExtra("Price", Price);
            startActivity(intent);
        });

        History.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessHomePage.this, BusinessHistoryActivity.class);
            startActivity(intent);
        });

        Support.setOnClickListener(View -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kardileajinkya@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I Need Support");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Team PrettyMeal\n");
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));

        });

        AboutUs.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessHomePage.this, AboutUsPage.class);
            startActivity(intent);
        });
    }
}