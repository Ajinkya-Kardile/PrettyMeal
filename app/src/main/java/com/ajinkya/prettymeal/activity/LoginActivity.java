package com.ajinkya.prettymeal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private Button LoginBtn;
    private TextView RegisterPageRedirecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginBtn = findViewById(R.id.LoginBtn);
        RegisterPageRedirecter = findViewById(R.id.RegisterPageRedirect);

        LoginBtn.setOnClickListener(View ->{
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        });

        RegisterPageRedirecter.setOnClickListener(View ->{
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        });



    }
}