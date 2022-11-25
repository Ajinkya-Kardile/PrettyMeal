package com.ajinkya.prettymeal.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ajinkya.prettymeal.HomeFragment;
import com.ajinkya.prettymeal.ProfileFragment;
import com.ajinkya.prettymeal.R;
import com.ajinkya.prettymeal.WalletFragment;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    private TextView LocationTextView;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locationManager;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Initialize();
        Permissions();
        Buttons();






    }

    private void Initialize() {

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);


        LocationTextView = findViewById(R.id.LocationTextView);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);


        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


        //location requirements
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void Buttons() {
        LocationTextView.setOnClickListener(View->{
            Intent intent = new Intent(MainActivity.this,AddressPicker.class);
            startActivity(intent);
        });
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
            Log.e(TAG, "CheckGpsStatus: Gps Is On" );
            return true;
        }
        else {
            Log.e(TAG, "CheckGpsStatus: Gps Is OFF" );
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
                Log.e(TAG, "onSuccess: Location settings (GPS) is ON.");
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onSuccess: Location settings (GPS) is OFF.");

                if (e instanceof ResolvableApiException){
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }







    @SuppressLint("NonConstantResourceId")
    private OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()){
            case R.id.id_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.id_wallet:
                selectedFragment = new WalletFragment();
                break;
            case R.id.id_profile:
                selectedFragment = new ProfileFragment();

        }

        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        return true;
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (!CheckGpsStatus()){
                    buttonSwitchGPS_ON();
                }
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        44);
            }
        }
    }
}