package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.businessAccount.BusinessLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private Button LoginBtn;
    private TextView RegisterPageNavigate, LoginAsMessOwner;
    private TextInputEditText EtEmail, EtPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private String Email, Password;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initialize();
        Buttons();


    }

    private void Initialize() {
        // initializing TextInputEditText
        EtEmail = findViewById(R.id.customerEmailLogin);
        EtPassword = findViewById(R.id.customerPasswordLogin);

        // initializing TextInputLayout
        layoutEmail = findViewById(R.id.customerEmailLoginLayout);
        layoutPassword = findViewById(R.id.customerPasswordLoginLayout);

        // initializing buttons/ hyperlinked text
        LoginBtn = findViewById(R.id.customerLoginBtn);
        RegisterPageNavigate = findViewById(R.id.RegisterPageRedirect);
        LoginAsMessOwner = findViewById(R.id.LoginAsMessOwner);

        // ProgressDialog creating...
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Validating...");
        progressDialog.setMessage("Please wait.... ");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

    }

    private boolean IsTextFieldValidate() {
        Email = Objects.requireNonNull(EtEmail.getText()).toString().trim();
        Password = Objects.requireNonNull(EtPassword.getText()).toString().trim();

        layoutEmail.setErrorEnabled(false);
        layoutPassword.setErrorEnabled(false);

        String EmailRegex = "^(.+)@(.+)$";
        Pattern EmailPattern = Pattern.compile(EmailRegex);
        Matcher EmailMatcher = EmailPattern.matcher(Email);


        if (Email.isEmpty() || !EmailMatcher.matches()) {
            layoutEmail.setError("Please Provide Validate Email");
            return false;
        } else if (Password.isEmpty()) {
            layoutPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void Buttons() {
        LoginBtn.setOnClickListener(View -> {
            if (IsTextFieldValidate()) {
                progressDialog.show();
                StartLogin(Email, Password);
            }
        });

        RegisterPageNavigate.setOnClickListener(View -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        LoginAsMessOwner.setOnClickListener(View -> {
            Intent intent = new Intent(LoginActivity.this, BusinessLoginActivity.class);
            startActivity(intent);
        });
    }

    private void StartLogin(String email, String password) {
        ArrayList<String> UserType = new ArrayList<>();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DatabaseReference UserTypeReference = FirebaseDatabase.getInstance().getReference().child("UserType").child(userUid);
                    UserTypeReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                            Log.e(TAG, "Value is: " + userType);
                            if (userType.equals("Customer")){
                                Login();
                            }else{
                                progressDialog.dismiss();
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("You can't able to use this account as a normal account")
                                        .show();
                                mAuth.signOut();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                            progressDialog.dismiss();
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Invalid username or password")
                            .setContentText("Please Enter Correct username or password")
                            .show();
                    EtPassword.setText("");
                    Log.e(TAG, "onComplete: Invalid username or password");

                }

            }

            private void Login() {
                if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                    progressDialog.dismiss();
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    progressDialog.dismiss();
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Email Not Verified..!")
                            .setContentText("Please verify your email")
                            .setConfirmText("Resend")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    sDialog
                                            .setTitleText("Mail sent Successfully!")
                                            .setContentText("Please check your Inbox as well as Spam folder")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void VerificationMailResend(String email) {

    }
}