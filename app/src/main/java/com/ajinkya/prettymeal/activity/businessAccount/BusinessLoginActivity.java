package com.ajinkya.prettymeal.activity.businessAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;

public class BusinessLoginActivity extends AppCompatActivity {
    private TextView businessRegPageNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        businessRegPageNavigator = findViewById(R.id.businessRegPageNavigator);
        businessRegPageNavigator.setOnClickListener(View->{
            Intent intent = new Intent(BusinessLoginActivity.this, BusinessRegisterActivity.class);
            startActivity(intent);
        });
    }
}