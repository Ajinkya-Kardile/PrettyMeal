package com.ajinkya.prettymeal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WalletFragment extends Fragment {

    private Button ContactUsBtn, ViewHistoryBtn;
    private TextView PlanDetailsTv, MealLeftTv, UserNameTv, StartDateTv, TotalPlatesTv, SelectPlanHeadTv;
    private LinearLayout Plan1Layout, Plan2Layout, PlanLayout;
    private DatabaseReference UserInfoRef;
    private String UserName, Membership, MealsLeft, TotalMeals, PlanStartDate;


    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);


        Initialize(view);
        FetchData();
        Buttons();
        return view;

    }

    private void FetchData() {
        String Current_UID = FirebaseAuth.getInstance().getUid();
        assert Current_UID != null;
        UserInfoRef = FirebaseDatabase.getInstance().getReference().child("Client_Application").child("Users").child(Current_UID).child("UserInfo");

        UserInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Membership = Objects.requireNonNull(snapshot.child("Membership").getValue()).toString();
                UserName = Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                MealsLeft = Objects.requireNonNull(snapshot.child("MealsLeft").getValue()).toString();
                TotalMeals = Objects.requireNonNull(snapshot.child("TotalMeals").getValue()).toString();
                PlanStartDate = Objects.requireNonNull(snapshot.child("PlanStartDate").getValue()).toString();
                CheckMemberShip();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckMemberShip() {
        if (Integer.parseInt(MealsLeft)>0){
            PlanLayout.setVisibility(View.GONE);
            SelectPlanHeadTv.setVisibility(View.GONE);


            PlanDetailsTv.setText(Membership);
            UserNameTv.setText(UserName);
            MealLeftTv.setText(MealsLeft);
            TotalPlatesTv.setText(TotalMeals);
            StartDateTv.setText(PlanStartDate);
        }else{
            PlanDetailsTv.setText("NA");
            UserNameTv.setText(UserName);
            MealLeftTv.setText("0");
            TotalPlatesTv.setText("0");
            StartDateTv.setText("NA");

            if (!Membership.equals("NA")){
                UserInfoRef.child("Membership").setValue("NA");
            }
        }
    }


    private void Initialize(View view) {
        ContactUsBtn = view.findViewById(R.id.NeedHelpBtn);
        ViewHistoryBtn = view.findViewById(R.id.ViewHistoryBtnWf);

//        Views
        UserNameTv = view.findViewById(R.id.UserNameWf);
        PlanDetailsTv = view.findViewById(R.id.PlanDetailsWF);
        MealLeftTv = view.findViewById(R.id.MealsLeftWf);
        TotalPlatesTv = view.findViewById(R.id.TotalPlatesWf);
        StartDateTv = view.findViewById(R.id.PlanStartDateWf);
        SelectPlanHeadTv = view.findViewById(R.id.GetPlanHeadWf);

//        layouts
        PlanLayout = view.findViewById(R.id.PlanLayoutWf);
        Plan1Layout = view.findViewById(R.id.Plan1LayoutWf);
        Plan2Layout = view.findViewById(R.id.Plan2LayoutWf);
    }

    private void Buttons() {
        ContactUsBtn.setOnClickListener(View -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kardileajinkya@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I Need Help");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Team PrettyMeal\n");
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        });


        ViewHistoryBtn.setOnClickListener(View -> {

        });

        Plan1Layout.setOnClickListener(View -> {
            DisplayPlanConformationDialog(30, 1500, "Silver");

        });

        Plan2Layout.setOnClickListener(View -> {
            DisplayPlanConformationDialog(60, 3000, "Gold");

        });
    }

    private void DisplayPlanConformationDialog(int Meals, int bill, String Membership){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String CurrentDate = df.format(c);
        Log.e(TAG, "DisplayPlanConformationDialog: "+CurrentDate );



        new SweetAlertDialog(getContext())
                .setTitleText("Please Conform and Pay")
                .setContentText("By conforming and paying you will get "+Meals+" meals with multiple mess choices")
                .setConfirmText("Pay Rs "+bill)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        UserInfoRef.child("Membership").setValue(Membership);
                        UserInfoRef.child("PlanStartDate").setValue(CurrentDate);
                        UserInfoRef.child("MealsLeft").setValue(Meals);
                        UserInfoRef.child("TotalMeals").setValue(Meals);
                        SelectPlanHeadTv.setVisibility(View.GONE);
                        PlanLayout.setVisibility(View.GONE);
                        sDialog
                                .setTitleText("Payment Successful!")
                                .setContentText("Enjoy Your Meals!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}