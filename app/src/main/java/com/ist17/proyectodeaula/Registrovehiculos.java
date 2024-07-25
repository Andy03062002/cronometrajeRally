package com.ist17.proyectodeaula;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

public class Registrovehiculos extends AppCompatActivity {

    EditText txtMarca, txtModelo, txtAnio, txtColor, txtCedula, txtPlaca;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrovehiculos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtMarca = findViewById(R.id.marca);
        txtModelo = findViewById(R.id.modelo);
        txtAnio = findViewById(R.id.anio);
        txtColor = findViewById(R.id.color);
        txtCedula = findViewById(R.id.cedula_participante);
        txtPlaca = findViewById(R.id.placa);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarVehiculo();
            }
        });
    }

    private void registrarVehiculo() {
        String URL = "http://192.168.30.110:8080/Proyecto4to/insertarVehiculo.php";
        final String marca = txtMarca.getText().toString().trim();
        final String modelo = txtModelo.getText().toString().trim();
        final String anio = txtAnio.getText().toString().trim();
        final String color = txtColor.getText().toString().trim();
        final String cedula = txtCedula.getText().toString().trim();
        final String placa = txtPlaca.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al registrar vehículo: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("marca", marca);
                params.put("modelo", modelo);
                params.put("anio", anio);
                params.put("color", color);
                params.put("cedula_participante", cedula);
                params.put("placa", placa);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
