package com.example.bottomnav.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bottomnav.MainActivity;
import com.example.bottomnav.R;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements LocationListener {

    private HomeViewModel homeViewModel;
    private Button btnGetCurrentLoc;
    private Button btnShowLoc;
    private TextView tv_MyCoordinates;
    LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tv_MyCoordinates = root.findViewById(R.id.tv_coordinates);
        btnShowLoc = root.findViewById(R.id.btn_showLoc);
        btnGetCurrentLoc = root.findViewById(R.id.btn_getCurrentLoc);
        // Runtime permission
        // Reference: YouTube Coding with Dev- Android Get Current Location
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        btnGetCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        return root;


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try{
            locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    5000, 5, HomeFragment.this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(this.getContext(), ""+location.getLatitude()+", "+location.getLongitude(),
//                Toast.LENGTH_LONG).show();
        try{
            Geocoder geocoder= new Geocoder(this.getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            @SuppressLint("DefaultLocale")
            String text = address + "\n" + "Latitude: " + String.format("%.2f", location.getLatitude()) +
                    "\n" + "Longitude: " + String.format("%.2f", location.getLongitude());
            tv_MyCoordinates.setText(text);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}