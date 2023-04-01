package com.ajinkya.prettymeal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ajinkya.prettymeal.activity.AboutUsPage;
import com.ajinkya.prettymeal.activity.LoginActivity;
import com.ajinkya.prettymeal.model.UserInfo;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private TextView TvProfileName, TvProfileEmail, TvMobileNo, TvDetailsEmail, TvEditProfile, TvAboutUs, TvLogoutBtn;
    private CircleImageView profileImage;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private String UserUid;
    private UserInfo userInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        Initialize(view);
        LoadingData();
        Buttons();
        return view;
    }


    private void Initialize(View view) {

        // TextView & Other View initialize
        profileImage = view.findViewById(R.id.CustomerProfile_image);
        TvProfileName = view.findViewById(R.id.CustomerProfileName);
        TvProfileEmail = view.findViewById(R.id.CustomerProfileEmail);
        TvMobileNo = view.findViewById(R.id.CustomerDetailNumber);
        TvDetailsEmail = view.findViewById(R.id.CustomerDetailEmail);
        TvEditProfile = view.findViewById(R.id.CustomerEditProfile);
        TvAboutUs = view.findViewById(R.id.CustomerAboutUs);
        TvLogoutBtn = view.findViewById(R.id.CustomerLogout);

        // Firebase requirement initialize
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        UserUid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();


    }

    private void LoadingData() {
        DatabaseReference RefUserInfo = firebaseDatabase.getReference().child("Client_Application").child("Users").child(UserUid).child("UserInfo");


        // Read from the database
        RefUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInfo = new UserInfo();
                userInfo.setUserProfileUrl(Objects.requireNonNull(dataSnapshot.child("ProfileImg").getValue()).toString());
                userInfo.setUserName(Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString());
                userInfo.setUserEmail(Objects.requireNonNull(dataSnapshot.child("Email").getValue()).toString());
                userInfo.setUserMobileNo(Objects.requireNonNull(dataSnapshot.child("PhoneNo").getValue()).toString());
                userInfo.setUserMembership(Objects.requireNonNull(dataSnapshot.child("Membership").getValue()).toString());

                // Set data to Views
                setValue(userInfo);
            }

            private void setValue(UserInfo userInfo) {
                if (!userInfo.getUserName().isEmpty()) {
                    if (!userInfo.getUserProfileUrl().isEmpty()) {
                        Glide.with(requireContext()).load(userInfo.getUserProfileUrl()).into(profileImage);
                    }
                    TvProfileName.setText(userInfo.getUserName());
                    TvProfileEmail.setText(userInfo.getUserEmail());
                    TvMobileNo.setText(userInfo.getUserMobileNo());
                    TvDetailsEmail.setText(userInfo.getUserEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }


    private void Buttons() {
        TvEditProfile.setOnClickListener(View -> {

        });


        TvAboutUs.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), AboutUsPage.class);
            startActivity(intent);
        });

        TvLogoutBtn.setOnClickListener(View -> {
            LogoutUser();
        });
    }


    private void LogoutUser() {
        new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you really want to exit..?")
                .setConfirmText("YES, LOG OUT!")
                .setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                })
                .show();
    }


}