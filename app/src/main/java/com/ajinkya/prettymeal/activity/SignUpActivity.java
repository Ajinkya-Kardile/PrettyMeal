package com.ajinkya.prettymeal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {
    private MaterialButton RegisterBtn;
    private TextView LoginPageRedirecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        LoginPageRedirecter = findViewById(R.id.LoginPageRedirect);
        
        RegisterBtn.setOnClickListener(View ->{
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        LoginPageRedirecter.setOnClickListener(View ->{
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        
        
    }
}