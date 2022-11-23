package com.ajinkya.prettymeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ajinkya.prettymeal.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {

    private TextView LogoutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        Initialize(view);
        Buttons();

        return view;
    }

    private void Initialize(View view) {
        LogoutBtn = view.findViewById(R.id.LogoutUser);
    }


    private void Buttons() {
        LogoutBtn.setOnClickListener(View -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }


}