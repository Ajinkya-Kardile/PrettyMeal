package com.ajinkya.prettymeal.activity.businessAccount;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ajinkya.prettymeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusinessRegisterActivity extends AppCompatActivity {
    private Button NextBtn;
    private TextView LoginPageRedirect;
    private TextInputEditText EtName, EtEmail, EtMobileNo, EtPassword, EtConformPassword;
    private TextInputLayout layoutName, layoutEmail, layoutMobileNo, layoutPassword, layoutConformPassword;
    private String Name, Email, MobileNo, Password, ConformPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);

        Initialize();
        Buttons();
    }

    private void Initialize() {

//        initializing TextInputEditText
        EtName = findViewById(R.id.BUserNameEditText);
        EtEmail = findViewById(R.id.BUserEmailEditText);
        EtMobileNo = findViewById(R.id.BUserMobileNoEditText);
        EtPassword = findViewById(R.id.BUserPasswordText);
        EtConformPassword = findViewById(R.id.BUserConformPasswordText);

//        initializing TextInputLayout
        layoutName = findViewById(R.id.BUserNameEditTextLayout);
        layoutEmail = findViewById(R.id.BUserEmailEditTextLayout);
        layoutMobileNo = findViewById(R.id.BUserMobileNoEditTextLayout);
        layoutPassword = findViewById(R.id.BUserPasswordTextLayout);
        layoutConformPassword = findViewById(R.id.BUserConformPasswordTextLayout);

//        initializing buttons/ hyperlinked text
        NextBtn = findViewById(R.id.BRegNextBtn);
        LoginPageRedirect = findViewById(R.id.BusinessLoginPageRedirect);

//        ProgressDialog creating...
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Validating Your Details...");
        progressDialog.setMessage("Please wait.... ");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
    }

    private void Buttons() {
        NextBtn.setOnClickListener(View -> {
            if (IsTextFieldValidate()) {
                progressDialog.show();
                CheckEmailAlreadyRegistered();
            }
        });

        LoginPageRedirect.setOnClickListener(View -> {
            Intent intent = new Intent(BusinessRegisterActivity.this, BusinessLoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void CheckEmailAlreadyRegistered() {
        auth.fetchSignInMethodsForEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            assert signInMethods != null;
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // User can sign in with email/password
                                Log.e(TAG, "Email Id already used for another account", task.getException());
                                progressDialog.dismiss();

                            }
                        } else {
                            // Go to the next page and pass all the information
                            progressDialog.dismiss();
                            PassDataOnNextActivity();
                        }
                    }
                });
    }


    private void PassDataOnNextActivity() {
        Log.e(TAG, "PassDataOnNextActivity: " );
        Intent intent = new Intent(BusinessRegisterActivity.this, MessInfoRegisterActivity.class);
        intent.putExtra("UserName",Name);
        intent.putExtra("UserEmail",Email);
        intent.putExtra("UserMobileNo",MobileNo);
        intent.putExtra("Password",Password);
        startActivity(intent);
        finish();
    }

    private boolean IsTextFieldValidate() {
        Name = Objects.requireNonNull(EtName.getText()).toString();
        Email = Objects.requireNonNull(EtEmail.getText()).toString().trim();
        MobileNo = Objects.requireNonNull(EtMobileNo.getText()).toString().trim();
        Password = Objects.requireNonNull(EtPassword.getText()).toString().trim();
        ConformPassword = Objects.requireNonNull(EtConformPassword.getText()).toString().trim();

        layoutName.setErrorEnabled(false);
        layoutEmail.setErrorEnabled(false);
        layoutMobileNo.setErrorEnabled(false);
        layoutPassword.setErrorEnabled(false);
        layoutConformPassword.setErrorEnabled(false);

        String EmailRegex = "^(.+)@(.+)$";
        Pattern EmailPattern = Pattern.compile(EmailRegex);
        Matcher EmailMatcher = EmailPattern.matcher(Email);


        if (Name.isEmpty()) {
            layoutName.setError("Please Provide Your Name");
            return false;
        } else if (Email.isEmpty() || !EmailMatcher.matches()) {
            layoutEmail.setError("Please Provide Validate Email");
            return false;
        } else if (MobileNo.length() < 10 || MobileNo.contains("+")) {
            layoutMobileNo.setError("Please Provide 10 digit MobileNo");
            return false;
        } else if (Password.isEmpty()) {
            layoutPassword.setError("Enter Password for Security");
            return false;
        } else if (!ConformPassword.equals(Password)) {
            layoutConformPassword.setError("Password does not match");
            EtConformPassword.setText("");
            ConformPassword = "";
            return false;
        }

        return true;
    }

}