package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.businessAccount.BusinessHomePage;
import com.ajinkya.prettymeal.activity.businessAccount.BusinessProfilePage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfileActivity extends AppCompatActivity {

    private Button SaveBtn;
    private TextInputEditText EtName, EtEmail, EtMobileNo;
    private TextInputLayout layoutName, layoutEmail, layoutMobileNo;
    private String Name, Email, MobileNo;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference UserInfoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.EditProfilePageToolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ExtractInfoFromIntent();
        Initialize();
        Buttons();

    }

    private void ExtractInfoFromIntent() {
        Intent intent = getIntent();
        Name = intent.getStringExtra("UserName");
        Email = intent.getStringExtra("UserEmail");
        MobileNo = intent.getStringExtra("UserMobileNo");

        Log.e(TAG, "ExtractInfoFromIntent: " + Name + "      " + Email);
    }

    private void Initialize() {

//        initializing TextInputEditText
        EtName = findViewById(R.id.EditProfileNameEditText);
        EtEmail = findViewById(R.id.EditProfileEmailEditText);
        EtMobileNo = findViewById(R.id.EditProfileMobileNoEditText);


//        initializing TextInputLayout
        layoutName = findViewById(R.id.EditProfileNameEditTextLayout);
        layoutEmail = findViewById(R.id.EditProfileEmailEditTextLayout);
        layoutMobileNo = findViewById(R.id.EditProfileMobileNoEditTextLayout);


//        set values to editText
        EtName.setText(Name);
        EtEmail.setText(Email);
        EtMobileNo.setText(MobileNo);


//        initializing buttons/ hyperlinked text
        SaveBtn = findViewById(R.id.EditProfileSaveBtn);

////        ProgressDialog creating...
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Validating Your Details...");
//        progressDialog.setMessage("Please wait.... ");
//        progressDialog.setCancelable(false);


//        firebase initialization
        String Current_Uid = FirebaseAuth.getInstance().getUid();
        assert Current_Uid != null;

        UserInfoRef = FirebaseDatabase.getInstance().getReference().child("Client_Application").child("Users").child(Current_Uid).child("UserInfo");

    }



    private void Buttons() {
        SaveBtn.setOnClickListener(View -> {
            if (IsTextFieldValidate()) {
                UserInfoRef.child("Name").setValue(Name);
                UserInfoRef.child("Email").setValue(Email);
                UserInfoRef.child("PhoneNo").setValue(MobileNo);
                Toast.makeText(this, "Data Saved SuccessFully", Toast.LENGTH_SHORT).show();
                onBackPressed();
//                Intent intent = new Intent(EditProfileActivity.this, BusinessHomePage.class);
//                startActivity(intent);
//                finish();
            } else Toast.makeText(this, "Enter all Details", Toast.LENGTH_SHORT).show();
        });


    }


    private boolean IsTextFieldValidate() {
        Name = Objects.requireNonNull(EtName.getText()).toString();
        Email = Objects.requireNonNull(EtEmail.getText()).toString().trim();
        MobileNo = Objects.requireNonNull(EtMobileNo.getText()).toString().trim();


        layoutName.setErrorEnabled(false);
        layoutEmail.setErrorEnabled(false);
        layoutMobileNo.setErrorEnabled(false);


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
        }

        return true;
    }



}