package com.ist17.proyectodeaula;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class validar_punto extends AppCompatActivity {

    private TextView textView2;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_validar_punto);

        textView2 = findViewById(R.id.textView2);
        Button button2 = findViewById(R.id.button2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        button2.setOnClickListener(view -> validarPuntoActual());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void validarPuntoActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double currentLat = location.getLatitude();
                        double currentLng = location.getLongitude();

                        // Aquí deberías recuperar la lista de polígonos de la base de datos
                        List<Polygon> polygons = obtenerPoligonos();

                        boolean puntoDentroDePoligono = false;
                        for (Polygon polygon : polygons) {
                            //if (polygon.isPointInPolygon(currentLat, currentLng)) {
                             //   puntoDentroDePoligono = true;
                              //  textView2.setText("Punto dentro del polígono: " + polygon.getNombre());
                               // break;
                            //}
                        }

                        if (!puntoDentroDePoligono) {
                            textView2.setText("El punto no está dentro de ningún polígono.");
                        }
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<Polygon> obtenerPoligonos() {
        List<Polygon> polygons = new ArrayList<>();

        // Polígono de prueba
        List<LatLng> puntos = new ArrayList<>();
        puntos.add(new LatLng(0.4069711691580755, -78.18099651485682));
        puntos.add(new LatLng(0.4064572038049041, -78.17890405654907));
        puntos.add(new LatLng(0.403104861473766, -78.17936271429062));
        puntos.add(new LatLng(0.40424175460278244, -78.18217400461435));
        puntos.add(new LatLng(0.4069711691580755, -78.18099651485682)); // Cierra el polígono

        //Polygon polygon = new Polygon("ist", puntos);
        //polygons.add(polygon);

        return polygons;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                validarPuntoActual();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
