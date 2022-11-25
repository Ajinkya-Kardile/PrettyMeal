package com.ajinkya.prettymeal.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.ajinkya.prettymeal.BuildConfig.MAPS_API_KEY;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.adevinta.leku.LocationPickerActivity;
import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.utils.GpsTracker;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddressPicker extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final int MAP_BUTTON_REQUEST_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Button LocationPickerBtn, UseLocationBtn, CancelBtn;
    private TextInputEditText AddressLine1, AddressLine2;
    private TextInputLayout AddressLine1Layout, AddressLine2Layout;
    private String AddressLine_1, AddressLine_2, FullAddress;
    private double Latitude = 0.0;
    private double Longitude = 0.0;
    private boolean ResultStatus;
    private LocationManager locationManager;
    private GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_picker);

//        ParseData();
        Initialize();
        Permissions();
        Buttons();

    }

//    private void ParseData() {
//        Intent intent = new Intent();
//        boolean cancelBtnStatus = intent.getBooleanExtra("CancelBtnEnable",true);
//    }

    private void Permissions() {
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            } else {
                if (!CheckGpsStatus()) {
                    buttonSwitchGPS_ON();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetLatLng();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Initialize() {
        //Toolbar setup -->
        Toolbar toolbar = findViewById(R.id.AddressPickerToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        //Other views -->
        LocationPickerBtn = findViewById(R.id.PickMyLocBtn);
        UseLocationBtn = findViewById(R.id.UseThisLocationBtn);
        CancelBtn = findViewById(R.id.CancelBtn);
        AddressLine1 = findViewById(R.id.AddressLine1);
        AddressLine2 = findViewById(R.id.AddressLine2);
        AddressLine1Layout = findViewById(R.id.AddressLine1Layout);
        AddressLine2Layout = findViewById(R.id.AddressLine2Layout);


        //location requirements
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    private void Buttons() {
        LocationPickerBtn.setOnClickListener(View -> {
            Intent locationPickerIntent = new LocationPickerActivity.Builder()
                    .withLocation(Latitude, Longitude)
                    .withGeolocApiKey(MAPS_API_KEY)
                    .withGooglePlacesApiKey(MAPS_API_KEY)
                    .withSearchZone("hi_IN")
                    .withDefaultLocaleSearchZone()
                    .shouldReturnOkOnBackPressed()
                    .withUnnamedRoadHidden()
                    .withGooglePlacesEnabled()
                    .build(this);

            startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);

        });

        UseLocationBtn.setOnClickListener(View -> {

        });

        CancelBtn.setOnClickListener(View -> {
            onBackPressed();
        });
    }

    private boolean CheckGpsStatus() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "CheckGpsStatus: Gps Is On");
            return true;
        } else {
            Log.e(TAG, "CheckGpsStatus: Gps Is OFF");
            return false;

        }
    }

    public void buttonSwitchGPS_ON() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(AddressPicker.this, "Location settings (GPS) is ON.", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddressPicker.this, "Location settings (GPS) is OFF.", Toast.LENGTH_SHORT).show();

                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(AddressPicker.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }


    private void GetLatLng() {
        if (ActivityCompat.checkSelfPermission(AddressPicker.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (CheckGpsStatus()) {
                for (int i = 0; i < 8; i++) {
                    if (Longitude == 0.0 && Latitude == 0.0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gpsTracker = new GpsTracker(AddressPicker.this);
                                if (gpsTracker.canGetLocation()) {
                                    Latitude = gpsTracker.getLatitude();
                                    Longitude = gpsTracker.getLongitude();
                                    AddressLine1Layout.setVisibility(View.VISIBLE);
                                    AddressLine1.setText(String.valueOf(Latitude + "," + Longitude));
                                } else {
                                    gpsTracker.showSettingsAlert();
                                }
                            }
                        }, 1000);
                    }

                }
            } else buttonSwitchGPS_ON();

        } else {
            ActivityCompat.requestPermissions(AddressPicker.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44);
        }


        if (ActivityCompat.checkSelfPermission(AddressPicker.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (CheckGpsStatus()) {
                gpsTracker = new GpsTracker(AddressPicker.this);
                if (gpsTracker.canGetLocation()) {
                    Latitude = gpsTracker.getLatitude();
                    Longitude = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } else buttonSwitchGPS_ON();

        } else {
            ActivityCompat.requestPermissions(AddressPicker.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!CheckGpsStatus()) {
                    buttonSwitchGPS_ON();
                }
            } else {
                ActivityCompat.requestPermissions(AddressPicker.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        44);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK");
            if (requestCode == 1) {
                double latitude = data.getDoubleExtra("latitude", 0.0);
                Log.e("LATITUDE****", Double.toString(latitude));
                double longitude = data.getDoubleExtra("longitude", 0.0);
                Log.e("LONGITUDE****", Double.toString(longitude));
                AddressLine2Layout.setVisibility(View.VISIBLE);
                AddressLine2.setText(String.valueOf(latitude + "," + longitude));

                String address = data.getStringExtra("location_address");
                Log.e("ADDRESS****", address.toString());
                String postalcode = data.getStringExtra("zipcode");
                Log.e("POSTALCODE****", postalcode.toString());

                Parcelable fullAddress = data.getParcelableExtra("address");
                Log.e("FULL ADDRESS****", fullAddress.toString());
                List<String> list = Arrays.asList(fullAddress.toString().split("\""));
                Log.e(TAG, "onActivityResult: " + list.get(1));
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("RESULT****", "CANCELLED");
        }
    }


}