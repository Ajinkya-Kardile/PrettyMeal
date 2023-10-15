package com.ajinkya.prettymeal.activity.businessAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BusinessWalletActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView walletBalTv, PlatesSoldTv;
    private Button WithdrawBtn;
    private String WalletBal, PlatesSold;
    DatabaseReference WalletInfoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_wallet);

        toolbar = findViewById(R.id.ClientHistoryToolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("Wallet");

        Initialize();
        FetchData();
        Buttons();
    }

    private void Buttons() {
        WithdrawBtn.setOnClickListener(View->{



            new SweetAlertDialog(this)
                    .setTitleText("Are you sure...")
                    .setContentText("By conforming you will receive your money in bank account")
                    .setConfirmText("Conform")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            WalletInfoRef.child("WalletBalance").setValue("0");
                            WalletInfoRef.child("TotalPlatesConsumed").setValue("0");
                            sDialog
                                    .setTitleText("Withdraw Successful!")
                                    .setContentText("")
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
        });
    }

    private void FetchData() {
        String Current_UID = FirebaseAuth.getInstance().getUid();
        WalletInfoRef = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_UID).child("WalletInfo");

        WalletInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WalletBal = Objects.requireNonNull(snapshot.child("WalletBalance").getValue()).toString();
                PlatesSold = Objects.requireNonNull(snapshot.child("TotalPlatesConsumed").getValue()).toString();
                SetText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SetText() {
        walletBalTv.setText(WalletBal);
        PlatesSoldTv.setText(PlatesSold);
    }

    private void Initialize() {
        walletBalTv = findViewById(R.id.BusinessWalletBal);
        PlatesSoldTv = findViewById(R.id.BusinessPlatesSold);
        WithdrawBtn = findViewById(R.id.WithdrawBalBtn);
    }
}