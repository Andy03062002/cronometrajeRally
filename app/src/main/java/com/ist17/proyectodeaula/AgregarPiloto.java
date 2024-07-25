package com.ist17.proyectodeaula;

import android.annotation.SuppressLint;
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

    EditText txtCedula, txtNombre, txtApellido, txtFechaNacimiento, txtNacionalidad, txtTelefono, txtCorreoElectronico, txtClave;
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
        txtNacionalidad = findViewById(R.id.nacionalidad);
        txtTelefono = findViewById(R.id.telefono);
        txtCorreoElectronico = findViewById(R.id.correo_electronico);
        txtClave = findViewById(R.id.clave);
        btnInsertar = findViewById(R.id.btnInsertar);

        agregarValidacionTiempoReal(txtCedula, "cedula");
        agregarValidacionTiempoReal(txtNombre, "nombre");
        agregarValidacionTiempoReal(txtApellido, "apellido");
        agregarValidacionTiempoReal(txtFechaNacimiento, "fechaNacimiento");
        agregarValidacionTiempoReal(txtNacionalidad, "nacionalidad");
        agregarValidacionTiempoReal(txtTelefono, "telefono");
        agregarValidacionTiempoReal(txtCorreoElectronico, "correoElectronico");
        agregarValidacionTiempoReal(txtClave, "clave");

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
            case "nacionalidad":
                return !valor.isEmpty() && valor.matches("[a-zA-Z]+");
            case "telefono":
                return !valor.isEmpty() && valor.matches("\\d+") && valor.length() == 10;
            case "correoElectronico":
                return !valor.isEmpty() && valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$");
            case "clave":
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
                validarCampo(txtNacionalidad.getText().toString().trim(), "nacionalidad") &&
                validarCampo(txtTelefono.getText().toString().trim(), "telefono") &&
                validarCampo(txtCorreoElectronico.getText().toString().trim(), "correoElectronico") &&
                validarCampo(txtClave.getText().toString().trim(), "clave");
    }

    private void insertarDatos() {
        String URL = "http://192.168.30.110:8080/Proyecto4to/insertarlocal.php";
        final String cedula = txtCedula.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellido = txtApellido.getText().toString().trim();
        final String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        final String nacionalidad = txtNacionalidad.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String correoElectronico = txtCorreoElectronico.getText().toString().trim();
        final String clave = txtClave.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al insertar datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cedula", cedula);
                params.put("nombre", nombre);
                params.put("apellido", apellido);
                params.put("fecha_nacimiento", fechaNacimiento);
                params.put("nacionalidad", nacionalidad);
                params.put("telefono", telefono);
                params.put("correo_electronico", correoElectronico);
                params.put("clave", clave);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
