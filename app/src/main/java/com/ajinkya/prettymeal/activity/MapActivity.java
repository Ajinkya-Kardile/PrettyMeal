package com.ajinkya.prettymeal.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.ajinkya.prettymeal.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Intent intent = getIntent();
        String Lat = intent.getStringExtra("MessLat");
        String Long = intent.getStringExtra("MessLong");
        String MessName = intent.getStringExtra("MessName");

        Log.e(TAG, "onMapReady: "+Double.parseDouble(Lat.trim()) );
        Log.e(TAG, "onMapReady: "+Double.parseDouble(Long.trim()) );
        // Add a marker in Mess and move the camera
        LatLng latLng = new LatLng(Double.parseDouble(Lat.trim()), Double.parseDouble(Long.trim()));
        Log.e(TAG, "onMapReady: "+latLng );
        mMap.addMarker(new MarkerOptions().position(latLng).title(MessName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }
}