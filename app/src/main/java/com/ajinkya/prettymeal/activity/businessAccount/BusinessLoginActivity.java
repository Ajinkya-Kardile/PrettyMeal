package com.ajinkya.prettymeal.activity.businessAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;

public class BusinessLoginActivity extends AppCompatActivity {
    private TextView businessRegPageNavigator;
    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        businessRegPageNavigator = findViewById(R.id.businessRegPageNavigator);
        LoginBtn = findViewById(R.id.BusinessLoginBtn);

        LoginBtn.setOnClickListener(View ->{
            Intent intent = new Intent(BusinessLoginActivity.this, BusinessHomePage.class);
            startActivity(intent);
        });



        businessRegPageNavigator.setOnClickListener(View->{
            Intent intent = new Intent(BusinessLoginActivity.this, BusinessRegisterActivity.class);
            startActivity(intent);
        });
    }
}