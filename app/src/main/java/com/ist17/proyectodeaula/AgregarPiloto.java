package com.ist17.proyectodeaula;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AgregarPiloto extends AppCompatActivity {

    EditText txtCedula, txtNombre, txtApellido, txtFechaNacimiento, txtGenero, txtNacionalidad, txtTelefono, txtCorreoElectronico, txtUsuario, txtContrasenia;
    Button btnInsertar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_piloto);

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
        btnInsertar = findViewById(R.id.btnInsertar);

        agregarValidacionTiempoReal(txtCedula, "cedula");
        agregarValidacionTiempoReal(txtNombre, "nombre");
        agregarValidacionTiempoReal(txtApellido, "apellido");
        agregarValidacionTiempoReal(txtFechaNacimiento, "fechaNacimiento");
        agregarValidacionTiempoReal(txtGenero, "genero");
        agregarValidacionTiempoReal(txtNacionalidad, "nacionalidad");
        agregarValidacionTiempoReal(txtTelefono, "telefono");
        agregarValidacionTiempoReal(txtCorreoElectronico, "correoElectronico");
        agregarValidacionTiempoReal(txtUsuario, "usuario");
        agregarValidacionTiempoReal(txtContrasenia, "contrasenia");

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    insertarDatos();
                }
            }
        });
    }

    private void agregarValidacionTiempoReal(EditText editText, String campo) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!validarCampo(editText.getText().toString().trim(), campo)) {
                    editText.setTextColor(Color.RED);
                } else {
                    editText.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validarCampo(String valor, String campo) {
        switch (campo) {
            case "cedula":
                return !valor.isEmpty() && valor.matches("\\d+") && valor.length() == 10 && !valor.endsWith("4") && (valor.startsWith("100") || valor.startsWith("04")) && !valor.equals("1111111111");
            case "nombre":
                return !valor.isEmpty() && valor.matches("[a-zA-Z]+") && valor.length() <= 50;
            case "apellido":
                return !valor.isEmpty() && valor.matches("[a-zA-Z]+") && valor.length() <= 50;
            case "fechaNacimiento":
                return !valor.isEmpty() && valor.matches("\\d{4}-\\d{2}-\\d{2}");
            case "genero":
                return !valor.isEmpty() && valor.matches("[a-zA-Z]+") && valor.length() <= 25;
            case "nacionalidad":
                return !valor.isEmpty() && valor.matches("[a-zA-Z]+") && valor.length() <= 25;
            case "telefono":
                return !valor.isEmpty() && valor.matches("\\d+") && valor.length() == 10;
            case "correoElectronico":
                return !valor.isEmpty() && valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$");
            case "usuario":
                return !valor.isEmpty() && valor.length() <= 25;
            case "contrasenia":
                return !valor.isEmpty(); // Puedes agregar más validaciones según tus requisitos
            default:
                return false;
        }
    }

    private boolean validarCampos() {
        return validarCampo(txtCedula.getText().toString().trim(), "cedula") &&
                validarCampo(txtNombre.getText().toString().trim(), "nombre") &&
                validarCampo(txtApellido.getText().toString().trim(), "apellido") &&
                validarCampo(txtFechaNacimiento.getText().toString().trim(), "fechaNacimiento") &&
                validarCampo(txtGenero.getText().toString().trim(), "genero") &&
                validarCampo(txtNacionalidad.getText().toString().trim(), "nacionalidad") &&
                validarCampo(txtTelefono.getText().toString().trim(), "telefono") &&
                validarCampo(txtCorreoElectronico.getText().toString().trim(), "correoElectronico") &&
                validarCampo(txtUsuario.getText().toString().trim(), "usuario") &&
                validarCampo(txtContrasenia.getText().toString().trim(), "contrasenia");
    }

    private void insertarDatos() {
        String url = "http://10.10.28.113:8080/Proyecto4to/insertarlocal.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AgregarPiloto.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AgregarPiloto.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgregarPiloto.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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