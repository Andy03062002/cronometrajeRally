package com.ist17.proyectodeaula;

import android.annotation.SuppressLint;
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

public class ModificarPiloto extends AppCompatActivity {

    EditText txtCedula, txtNombre, txtApellido, txtFechaNacimiento, txtGenero, txtNacionalidad, txtTelefono, txtCorreoElectronico, txtUsuario, txtContrasenia;
    Button btnModificar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_piloto);

        txtCedula = findViewById(R.id.cedula);
        txtNombre = findViewById(R.id.nombre);
        txtApellido = findViewById(R.id.apellido);
        txtFechaNacimiento = findViewById(R.id.fecha_nacimiento);
        txtGenero = findViewById(R.id.genero);
        txtNacionalidad = findViewById(R.id.nacionalidad);
        txtTelefono = findViewById(R.id.telefono);
        txtCorreoElectronico = findViewById(R.id.correo_electronico);
        txtUsuario = findViewById(R.id.usuario);
        txtContrasenia = findViewById(R.id.contrasenia);
        btnModificar = findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatos();
            }
        });
    }

    private void modificarDatos() {
        String url = "http://10.10.28.113:8080/Proyecto4to/modificarlocal.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ModificarPiloto.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ModificarPiloto.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cedula", txtCedula.getText().toString().trim());
                params.put("nombre", txtNombre.getText().toString().trim());
                params.put("apellido", txtApellido.getText().toString().trim());
                params.put("fecha_nacimiento", txtFechaNacimiento.getText().toString().trim());
                params.put("genero", txtGenero.getText().toString().trim());
                params.put("nacionalidad", txtNacionalidad.getText().toString().trim());
                params.put("telefono", txtTelefono.getText().toString().trim());
                params.put("correo_electronico", txtCorreoElectronico.getText().toString().trim());
                params.put("usuario", txtUsuario.getText().toString().trim());
                params.put("contrasenia", txtContrasenia.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
