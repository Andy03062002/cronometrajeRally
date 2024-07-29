package com.ist17.proyectodeaula;

import android.content.Intent;
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

        // Ajustar el padding de la vista principal para evitar el contenido superpuesto por las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar los elementos de la interfaz de usuario
        txtMarca = findViewById(R.id.marca);
        txtModelo = findViewById(R.id.modelo);
        txtAnio = findViewById(R.id.anio);
        txtColor = findViewById(R.id.color);
        txtCedula = findViewById(R.id.cedula_participante);
        txtPlaca = findViewById(R.id.placa);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Configurar el evento click para el botón Registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarVehiculo();
            }
        });
    }
    private void registrarVehiculo() {
        // Obtener los valores de los campos de texto
        final String marca = txtMarca.getText().toString().trim();
        final String modelo = txtModelo.getText().toString().trim();
        final String anio = txtAnio.getText().toString().trim();
        final String color = txtColor.getText().toString().trim();
        final String cedula = txtCedula.getText().toString().trim();
        final String placa = txtPlaca.getText().toString().trim();

        // Validar los campos
        if (marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || color.isEmpty() || cedula.isEmpty() || placa.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato de la placa (Ejemplo: ABC-1234)
        if (!placa.matches("[A-Z]{3}-[0-9]{4}")) {
            Toast.makeText(getApplicationContext(), "El formato de la placa no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el año (Ejemplo: Año entre 1900 y el año actual)
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        try {
            int anioInt = Integer.parseInt(anio);
            if (anioInt < 1900 || anioInt > currentYear) {
                Toast.makeText(getApplicationContext(), "El año debe estar entre 1900 y " + currentYear, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "El año debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL del servidor donde se enviarán los datos
        String URL = "http://192.168.30.110:8080/Proyecto4to/insertarVehiculo.php";

        // Crear una solicitud POST usando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registrovehiculos.this, MENU_LOGIN.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al registrar vehículo: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Configurar los parámetros que se enviarán en la solicitud
                Map<String, String> params = new HashMap<>();
                params.put("marca", marca);
                params.put("modelo", modelo);
                params.put("anio", anio);
                params.put("color", color);
                params.put("cedula_participante", cedula); // Asegúrate de que el nombre coincida con el PHP
                params.put("placa", placa);
                return params;
            }
        };

        // Crear una cola de solicitudes y agregar la solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}