package com.ist17.proyectodeaula;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class inscripcion extends AppCompatActivity {

    EditText txtCedula;
    Button btnEnviarInscripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion);

        txtCedula = findViewById(R.id.editTextText);
        btnEnviarInscripcion = findViewById(R.id.button3);

        btnEnviarInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cedula = txtCedula.getText().toString().trim();
                if (validarCedula(cedula)) {
                    enviarInscripcion(cedula);
                } else {
                    Toast.makeText(inscripcion.this, "Por favor, ingresa una cédula válida", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validarCedula(String cedula) {
        return !cedula.isEmpty() && cedula.matches("\\d{10}");
    }

    private void enviarInscripcion(final String cedula) {
        String url = "http://10.10.28.113:8080/Proyecto4to/inscripcion.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(inscripcion.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inscripcion.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cedula", cedula);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
