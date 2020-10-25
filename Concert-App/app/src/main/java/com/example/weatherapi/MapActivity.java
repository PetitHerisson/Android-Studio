package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String coordinates = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] geocod = coordinates.split("/");
        lat = Double.parseDouble(geocod[0]);
        lng = Double.parseDouble(geocod[1]);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latlng = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions()
                .position(latlng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
    }
}