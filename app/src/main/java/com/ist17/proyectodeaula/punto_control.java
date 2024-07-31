package com.ist17.proyectodeaula;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class punto_control extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText txt_lat, txt_long;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_punto_control);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            txt_lat = (EditText) findViewById(R.id.txt_latitud);
            txt_long = (EditText) findViewById(R.id.txt_longitud);
            SupportMapFragment mapF = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapF.getMapAsync(this);


            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    mMap = googleMap;
    this.mMap.setOnMapClickListener(this);
    this.mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
       txt_lat.setText(""+latLng.latitude);
       txt_long.setText(""+latLng.longitude);

       mMap.clear();
       LatLng pc = new LatLng(latLng.latitude,latLng.longitude);
       mMap.addMarker(new MarkerOptions().position(pc).title(""));
       mMap.moveCamera(CameraUpdateFactory.newLatLng(pc));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txt_lat.setText(""+latLng.latitude);
        txt_long.setText(""+latLng.longitude);

        mMap.clear();
        LatLng pc = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(pc).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pc));
    }
}