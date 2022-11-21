package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajinkya.prettymeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private Button RegisterBtn;
    private TextView LoginPageRedirect;
    private TextInputEditText EtName, EtEmail, EtMobileNo, EtPassword, EtConformPassword;
    private TextInputLayout layoutName, layoutEmail, layoutMobileNo, layoutPassword, layoutConformPassword;
    private String Name, Email, MobileNo, Password, ConformPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Initialize();
        Buttons();


    }


    private void Initialize() {

//        initializing TextInputEditText
        EtName = findViewById(R.id.customerNameReg);
        EtEmail = findViewById(R.id.customerEmailReg);
        EtMobileNo = findViewById(R.id.customerMobileReg);
        EtPassword = findViewById(R.id.customerPasswordReg);
        EtConformPassword = findViewById(R.id.customerPasswordConform);

//        initializing TextInputLayout
        layoutName = findViewById(R.id.customerNameRegLayout);
        layoutEmail = findViewById(R.id.customerEmailRegLayout);
        layoutMobileNo = findViewById(R.id.customerMobileRegLayout);
        layoutPassword = findViewById(R.id.customerPasswordRegLayout);
        layoutConformPassword = findViewById(R.id.customerPasswordConformLayout);

//        initializing buttons/ hyperlinked text
        RegisterBtn = findViewById(R.id.customerRegisterBtn);
        LoginPageRedirect = findViewById(R.id.LoginPageRedirect);

//        ProgressDialog creating...
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering Your Details...");
        progressDialog.setMessage("Please wait.... ");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
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


    private void Buttons() {
        RegisterBtn.setOnClickListener(View -> {
            if (IsTextFieldValidate()) {
                progressDialog.show();
                StartRegister();
            }
        });

        LoginPageRedirect.setOnClickListener(View -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void StartRegister() {
//        if (isNewAccount(Email)) {
            auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    RegisterUserInDatabase(Name, Email, MobileNo);
                                } else {
                                    layoutEmail.setError("Provide correct Email");
                                    Toast.makeText(SignUpActivity.this, "Wrong email address", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        try
                        {
                            throw Objects.requireNonNull(task.getException());
                        }
                        catch (FirebaseAuthUserCollisionException existEmail)
                        {
                            Log.e(TAG, "StartRegister: Email id already exist..., Please try with different Email");
                            Toast.makeText(SignUpActivity.this, "The Email is already Used, Please try with different Email", Toast.LENGTH_LONG).show();
                            layoutEmail.setError("Email already used for another user");
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, "onComplete: " + e.getMessage());
                            Toast.makeText(SignUpActivity.this, "Something went wrong..., Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


//        } else {
//            progressDialog.dismiss();
//            Log.e(TAG, "StartRegister: Email id already exist..., Please try with different Email");
//            Toast.makeText(this, "The Email is already Used, Please try with different Email", Toast.LENGTH_LONG).show();
//        }
    }

    private boolean isNewAccount(String email) {
        //check email already exist or not.
        final boolean[] isNewUser = {false};
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()) isNewUser[0] = true;
                        else isNewUser[0] = false;
                    }
                });
        if (isNewUser[0]) {
            return true;
        } else return false;
    }

    private void RegisterUserInDatabase(String name, String email, String mobileNo) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String Current_Uid = currentUser.getUid();

        // Store user type in database
        DatabaseReference UserTypeReference = FirebaseDatabase.getInstance().getReference().child("UserType");
        UserTypeReference.child(email).setValue("Customer");


        // Store user data in database
        DatabaseReference userInfoReference = FirebaseDatabase.getInstance().getReference().child("ClientApplication").child("Users").child(Current_Uid).child("UserInfo");
        Log.e(TAG, "RegisterPhone: "+ userInfoReference);
        HashMap<String, String> user = new HashMap<>();
        user.put("Name",name);
        user.put("Email",email);
        user.put("PhoneNo",mobileNo);
        user.put("ProfileImg","");
        user.put("Membership","NA");

        userInfoReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignUpActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();



                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Please Verify Your Email");
                    builder.setMessage("Verification link is sent on your mail. \n If mail is not visible in 'inbox', then check your 'Spam' folder");
                    builder.setPositiveButton("Okk", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Filed to register info",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}