package com.example.modul11;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView latitude, longitude, altitude, akurasi, alamat;
    private Button btnFind;
    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        altitude = findViewById(R.id.altitude);
        akurasi = findViewById(R.id.akurasi);
        alamat = findViewById(R.id.alamat);
        btnFind = findViewById(R.id.btn_find);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Izin lokasi tidak di aktifkan!", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getLocation();
                }
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
        } else {
            locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location!=null) {
                    latitude.setText(String.valueOf(location.getLatitude()));
                    longitude.setText(String.valueOf(location.getLongitude()));
                    altitude.setText(String.valueOf(location.getAltitude()));
                    akurasi.setText(location.getAccuracy() + "%");
                    alamat.setText(getCompleteAddressString(location.getLatitude(), location.getLongitude()))   ;
                } else {
                    Toast.makeText(getApplicationContext(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE)  {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        addresses = geocoder.getFromLocation(latitude, longitude, 1);
//        return addresses.get(0).getAddressLine(0);
    }
}
