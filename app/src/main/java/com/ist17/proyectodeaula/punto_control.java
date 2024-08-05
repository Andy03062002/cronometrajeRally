package com.ist17.proyectodeaula;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // Construir la cadena JSON con los puntos del polígono
            JSONArray puntosJsonArray = new JSONArray();
            for (LatLng punto : polygonPoints) {
                JSONObject puntoJson = new JSONObject();
                try {
                    puntoJson.put("latitude", punto.latitude);
                    puntoJson.put("longitude", punto.longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                puntosJsonArray.put(puntoJson);
            }

            // Convertir JSONArray a String
            String puntosJsonString = puntosJsonArray.toString();

            // Llamar al método para insertar los datos
            insertarDatos(nombrePoligono, puntosJsonString);

            // Mostrar el nombre del polígono y los puntos en un Toast
            Toast.makeText(this, "Polígono " + nombrePoligono + " guardado.", Toast.LENGTH_LONG).show();
        } else if (!isPolygonClosed) {
            Toast.makeText(this, "Primero cierra el polígono antes de guardarlo.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, ingresa un nombre para el polígono.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertarDatos(String nombrePoligono, String puntosJson) {
        String url = "http://10.10.24.218:8088/Proyecto4to/guardarPC.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(punto_control.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(punto_control.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombrePoligono", nombrePoligono);
                params.put("puntos", puntosJson);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
