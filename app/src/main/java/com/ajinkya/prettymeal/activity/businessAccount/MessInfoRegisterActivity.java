package com.ajinkya.prettymeal.activity.businessAccount;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.activity.AddressPicker;
import com.ajinkya.prettymeal.activity.LoginActivity;
import com.ajinkya.prettymeal.activity.MainActivity;
import com.ajinkya.prettymeal.activity.SignUpActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MessInfoRegisterActivity extends AppCompatActivity {
    private Button RegisterBtn, AddressPickerBtn;
    private TextInputEditText EtMessName, EtSupportEmail, EtSupportMobileNo, EtMessDescription;
    private TextView MessAddressTextView;
    private TextInputLayout layoutMessName, layoutSupportEmail, layoutSupportMobileNo, layoutMessDescription;
    private Spinner MessTypeSpinner;
    private String UserName, UserEmail, UserMobileNo, UserPassword, MessName, SupportEmail, SupportMobileNo, MessDescription, MessType, MessFullAddress,MessShortAddress, MessLat, MessLong;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationManager locationManager;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_info_register);


        ExtractInfoFromIntent();
        Initialize();
        Permissions();
        Buttons();

    }


    private void ExtractInfoFromIntent() {
        Intent intent = new Intent();
        UserName = intent.getStringExtra("UserName");
        UserEmail = intent.getStringExtra("UserEmail");
        UserMobileNo = intent.getStringExtra("UserMobileNo");
        UserPassword = intent.getStringExtra("Password");
    }


    private void Initialize() {
//        initializing TextInputEditText
        EtMessName = findViewById(R.id.RegMessName);
        EtSupportEmail = findViewById(R.id.RegMessEmail);
        EtSupportMobileNo = findViewById(R.id.RegMessPhoneNo);
        EtMessDescription = findViewById(R.id.RegMessDescription);
        MessTypeSpinner = findViewById(R.id.MessTypeSpinner);

        MessAddressTextView = findViewById(R.id.RegMessAddressTextView);



//        initializing TextInputLayout
        layoutMessName = findViewById(R.id.RegMessNameLayout);
        layoutSupportEmail = findViewById(R.id.RegMessEmailLayout);
        layoutSupportMobileNo = findViewById(R.id.RegMessPhoneNoLayout);
        layoutMessDescription = findViewById(R.id.RegMessDescriptionLayout);


//        initializing buttons/ hyperlinked text
        RegisterBtn = findViewById(R.id.businessRegisterBtn);
        AddressPickerBtn = findViewById(R.id.MessLocationPickerBtn);

//        ProgressDialog creating...
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Validating Your Details...");
        progressDialog.setMessage("Please wait.... ");
        progressDialog.setCancelable(false);

//        firebase initialization
        auth = FirebaseAuth.getInstance();



        //location requirements
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void Buttons() {
        RegisterBtn.setOnClickListener(View -> {
            if (IsTextFieldValidate()) {
                progressDialog.show();
                StartRegister();
            }
        });

        AddressPickerBtn.setOnClickListener(View -> {
            Intent intent = new Intent(MessInfoRegisterActivity.this,AddressPicker.class);
            intent.putExtra("CancelBtnEnable",false);
            startActivityForResult(intent,10);
        });
    }



    private boolean IsTextFieldValidate() {
        MessName = Objects.requireNonNull(EtMessName.getText()).toString();
        SupportEmail = Objects.requireNonNull(EtSupportEmail.getText()).toString().trim();
        SupportMobileNo = Objects.requireNonNull(EtSupportMobileNo.getText()).toString().trim();
        MessDescription = Objects.requireNonNull(EtMessDescription.getText()).toString().trim();
        MessType = Objects.requireNonNull(MessTypeSpinner.getSelectedItem().toString());

        layoutMessName.setErrorEnabled(false);
        layoutSupportEmail.setErrorEnabled(false);
        layoutSupportMobileNo.setErrorEnabled(false);
        layoutMessDescription.setErrorEnabled(false);
//        layoutMessName.setErrorEnabled(false);

        String EmailRegex = "^(.+)@(.+)$";
        Pattern EmailPattern = Pattern.compile(EmailRegex);
        Matcher EmailMatcher = EmailPattern.matcher(SupportEmail);


        if (MessName.isEmpty()) {
            layoutMessName.setError("Please Provide Mess Name");
            return false;
        } else if (SupportEmail.isEmpty() || !EmailMatcher.matches()) {
            layoutSupportEmail.setError("Please Provide Validate Email");
            return false;
        } else if (SupportMobileNo.length() < 10 || SupportMobileNo.contains("+")) {
            layoutSupportMobileNo.setError("Please Provide 10 digit MobileNo");
            return false;
        } else if (MessDescription.isEmpty()) {
            layoutMessDescription.setError("Enter Your Mess Information");
            return false;
        } else if (MessType.isEmpty()) {
            Toast.makeText(this, "Select mess type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (MessFullAddress.isEmpty()){
            Toast.makeText(this, "Pick mess location", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 &&  resultCode==RESULT_OK){
            if (data!=null){

                // Get data form result ==>
                double Latitude = data.getExtras().getDouble("Latitude",0.0);
                double Longitude = data.getExtras().getDouble("Longitude",0.0);
//                String AddressLine1 = data.getExtras().getString("AddressLine1","India");
//                String AddressLine2 = data.getExtras().getString("AddressLine2","India");
                MessFullAddress = data.getExtras().getString("FullAddress","India");
                MessShortAddress = data.getExtras().getString("ShortAddress","India");


                MessLat = String.valueOf(Latitude);
                MessLong = String.valueOf(Longitude);
                MessAddressTextView.setVisibility(View.VISIBLE);
                MessAddressTextView.setText(MessFullAddress);
            }
        }
    }



    private void Permissions() {
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else {
                if (!CheckGpsStatus()){
                    buttonSwitchGPS_ON();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean CheckGpsStatus(){
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.e(MotionEffect.TAG, "CheckGpsStatus: Gps Is On" );
            return true;
        }
        else {
            Log.e(MotionEffect.TAG, "CheckGpsStatus: Gps Is OFF" );
            return false;

        }
    }
    public void buttonSwitchGPS_ON(){

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e(MotionEffect.TAG, "onSuccess: Location settings (GPS) is ON.");
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(MotionEffect.TAG, "onSuccess: Location settings (GPS) is OFF.");

                if (e instanceof ResolvableApiException){
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(MessInfoRegisterActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }


    private void StartRegister() {
        auth.createUserWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        RegisterUserInDatabase();
                    } else {
//                        layoutEmail.setError("Provide correct Email");
                        new SweetAlertDialog(MessInfoRegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error...")
                                .setContentText("Email id not exist!")
                                .show();
                        Log.e(TAG, "onComplete: Wrong email address");
                    }

                });

            } else {
                progressDialog.dismiss();
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthUserCollisionException existEmail) {
                    Log.e(TAG, "StartRegister: Email id already exist..., Please try with different Email");
//                    layoutEmail.setError("Email already used for another user");
                    new SweetAlertDialog(MessInfoRegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Email is already Used...")
                            .setContentText("Please try with different Email")
                            .show();
                } catch (Exception e) {
                    Log.d(TAG, "onComplete: " + e.getMessage());
                    new SweetAlertDialog(MessInfoRegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }

            }

        });
    }



    private void RegisterUserInDatabase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String Current_Uid = currentUser.getUid();

        // Store user type in database
        DatabaseReference UserTypeReference = FirebaseDatabase.getInstance().getReference().child("UserType");
        UserTypeReference.child(Current_Uid).setValue("MessOwner");


        // Store user data in database
        DatabaseReference userInfoReference = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("UserInfo");
        Log.e(TAG, "RegisterPhone: " + userInfoReference);
        HashMap<String, String> user = new HashMap<>();
        user.put("Name", UserName);
        user.put("Email", UserEmail);
        user.put("PhoneNo", UserMobileNo);
        user.put("ProfileImg", "");
        user.put("Membership", "NA");



        userInfoReference.setValue(user);


        // update firebase database ==>
        DatabaseReference MessInfoRef = FirebaseDatabase.getInstance().getReference().child("Business_Application").child("Users").child(Current_Uid).child("MessDetails");

        HashMap<String, String> MessDetails = new HashMap<>();
        MessDetails.put("Latitude", MessLat);
        MessDetails.put("Longitude", MessLong);
        MessDetails.put("FullAddress", MessFullAddress);
        MessDetails.put("ShortAddress", MessShortAddress);
        MessDetails.put("MessName", MessShortAddress);
        MessDetails.put("MessType", MessShortAddress);
        MessDetails.put("SupportEmail", MessShortAddress);
        MessDetails.put("SupportPhoneNo", MessShortAddress);
        MessDetails.put("MessDesc", MessShortAddress);


        MessInfoRef.setValue(MessDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                new SweetAlertDialog(MessInfoRegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Successfully Registered\n Please Verify your Email")
                        .setContentText("Verification link sent on your email.\nIf mail not visible inside Inbox then check the Spam folder")
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(MessInfoRegisterActivity.this, BusinessLoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .show();
                Log.e(TAG, "onComplete: Registered Successfully....");
            } else {
                progressDialog.dismiss();
                new SweetAlertDialog(MessInfoRegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
                Log.e(TAG, "onComplete: Filed to register info");
            }
        });



    }





}