package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.businessAccount.BusinessHomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int SPLASH_SCREEN_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(currentUser ==null){
                    Intent in = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(in);
                    finish();
                }else if(currentUser.isEmailVerified()){
                    String userUid = currentUser.getUid();
                    DatabaseReference UserTypeReference = FirebaseDatabase.getInstance().getReference().child("UserType").child(userUid);
                    UserTypeReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                            Log.e(TAG, "Value is: " + userType);
                            if (userType.equals("Customer")){
                                Intent in = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(in);
                                finish();
                            }else{
                                Intent in = new Intent(SplashScreenActivity.this, BusinessHomePage.class);
                                startActivity(in);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                }
                else {
                    Intent in = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);



    }
}
