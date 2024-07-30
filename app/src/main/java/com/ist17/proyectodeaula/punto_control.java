package com.ist17.proyectodeaula;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class punto_control extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private EditText txtNombrePC;

    private GoogleMap mMap;
    private List<LatLng> polygonPoints = new ArrayList<>();
    private Polygon polygon;
    private boolean isPolygonClosed = false;
    private LatLng firstPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_punto_control);

        txtNombrePC = findViewById(R.id.txt_nombre_pc);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(v -> guardarPoligono());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if (isPolygonClosed) {
            Toast.makeText(this, "Polígono ya cerrado. No se pueden agregar más puntos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (polygonPoints.isEmpty()) {
            firstPoint = latLng;
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title("Punto del polígono"));
        polygonPoints.add(latLng);

        if (firstPoint != null && firstPoint.equals(latLng) && polygonPoints.size() > 2) {
            cerrarPoligono();
        }
    }

    private void cerrarPoligono() {
        if (polygonPoints.size() > 2) {
            PolygonOptions polygonOptions = new PolygonOptions().addAll(polygonPoints);
            polygon = mMap.addPolygon(polygonOptions);
            isPolygonClosed = true;
            Toast.makeText(this, "Polígono cerrado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Se necesitan al menos 3 puntos para crear un polígono.", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarPoligono() {
        if (isPolygonClosed && txtNombrePC.getText() != null && !txtNombrePC.getText().toString().isEmpty()) {
            String nombrePoligono = txtNombrePC.getText().toString();

            // Construir la cadena con los puntos del polígono
            StringBuilder puntos = new StringBuilder("Puntos: ");
            for (LatLng punto : polygonPoints) {
                puntos.append("\nLat: ").append(punto.latitude).append(", Lng: ").append(punto.longitude);
            }

            // Mostrar el nombre del polígono y los puntos en un Toast
            Toast.makeText(this, "Polígono " + nombrePoligono + " guardado." + "\n" + puntos.toString(), Toast.LENGTH_LONG).show();

            // Aquí puedes manejar la lógica para guardar el nombre y los puntos del polígono
            // por ejemplo, almacenarlos en una base de datos o enviarlos a un servidor.
        } else if (!isPolygonClosed) {
            Toast.makeText(this, "Primero cierra el polígono antes de guardarlo.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, ingresa un nombre para el polígono.", Toast.LENGTH_SHORT).show();
        }
    }

}
