package com.ist17.proyectodeaula;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class validar_punto extends AppCompatActivity {

    private TextView textView2;
    private Spinner spinnerPoligonos;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Poligono> poligonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_validar_punto);

        textView2 = findViewById(R.id.textView2);
        spinnerPoligonos = findViewById(R.id.spinner_poligonos);
        Button button2 = findViewById(R.id.button2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        button2.setOnClickListener(view -> validarPuntoActual());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        obtenerPoligonosDesdeServidor();
    }

    private void obtenerPoligonosDesdeServidor() {
        String url = "http://192.168.3.34:8088/Proyecto4to/obtenerPoligonos.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            poligonos = new ArrayList<>();
                            List<String> nombresPoligonos = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                String ubicacion = jsonObject.getString("ubicacion");

                                Log.d("Poligono", "Nombre: " + nombre + ", Ubicacion: " + ubicacion);

                                String[] puntosString = ubicacion.split(";");
                                List<LatLng> puntos = new ArrayList<>();
                                for (String puntoString : puntosString) {
                                    if (!puntoString.isEmpty()) {
                                        String[] latLng = puntoString.split(",");
                                        if (latLng.length == 2) {
                                            try {
                                                double lat = Double.parseDouble(latLng[0]);
                                                double lng = Double.parseDouble(latLng[1]);
                                                puntos.add(new LatLng(lat, lng));
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                Poligono poligono = new Poligono(nombre, puntos);
                                poligonos.add(poligono);
                                nombresPoligonos.add(nombre);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(validar_punto.this, android.R.layout.simple_spinner_item, nombresPoligonos);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPoligonos.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(validar_punto.this, "Error al parsear la respuesta JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(validar_punto.this, "Error en la respuesta del servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
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
                        LatLng currentLocation = new LatLng(currentLat, currentLng);

                        if (poligonos != null && !poligonos.isEmpty()) {
                            Poligono poligonoSeleccionado = poligonos.get(spinnerPoligonos.getSelectedItemPosition());
                            List<LatLng> puntos = poligonoSeleccionado.getPuntos();

                            if (PolygonUtils.isPointInPolygon(currentLocation, puntos)) {
                                textView2.setText("Punto dentro del polígono: " + poligonoSeleccionado.getNombre());
                                guardarPasoPunto(currentLocation);
                            } else {
                                textView2.setText("El punto no está dentro del polígono seleccionado.");
                            }
                        } else {
                            Toast.makeText(this, "No hay polígonos disponibles para validar.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void guardarPasoPunto(LatLng currentLocation) {
        String url = "http://192.168.3.34:8088/Proyecto4to/guardarPasoPunto.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(validar_punto.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(validar_punto.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_inscripcion", "1"); // Reemplaza con el ID de inscripción correspondiente
                params.put("ubicacion", currentLocation.latitude + "," + currentLocation.longitude);
                params.put("horaDePaso", String.valueOf(System.currentTimeMillis())); // Timestamp actual
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
