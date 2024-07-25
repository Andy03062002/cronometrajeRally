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

    EditText txtCedula, txtNombre, txtApellido, txtFechaNacimiento, txtNacionalidad, txtTelefono, txtCorreoElectronico;
    Button btnModificar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_piloto);

        // Inicializar vistas
        txtCedula = findViewById(R.id.cedula);
        txtNombre = findViewById(R.id.nombre);
        txtApellido = findViewById(R.id.apellido);
        txtFechaNacimiento = findViewById(R.id.fecha_nacimiento);
        txtNacionalidad = findViewById(R.id.nacionalidad);
        txtTelefono = findViewById(R.id.telefono);
        txtCorreoElectronico = findViewById(R.id.correo_electronico);
        btnModificar = findViewById(R.id.btnModificar);

        // Configurar listener para el botón de modificar
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    modificarDatos();
                }
            }
        });
    }

    private boolean validarCampos() {
        String cedula = txtCedula.getText().toString().trim();
        String nombre = txtNombre.getText().toString().trim();
        String apellido = txtApellido.getText().toString().trim();
        String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        String nacionalidad = txtNacionalidad.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correoElectronico = txtCorreoElectronico.getText().toString().trim();

        // Validación de la cédula
        if (cedula.isEmpty() || !cedula.matches("\\d+") || cedula.length() != 10 || cedula.endsWith("4") || (!cedula.startsWith("100") && !cedula.startsWith("04"))) {
            Toast.makeText(this, "Cédula inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación de que no haya cédulas duplicadas (esto debería hacerse en el servidor)
        if (cedula.equals("1111111111")) {
            Toast.makeText(this, "Cédula duplicada", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nombre.isEmpty() || !nombre.matches("[a-zA-Z]+") || nombre.length() > 50) {
            Toast.makeText(this, "Nombre inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (apellido.isEmpty() || !apellido.matches("[a-zA-Z]+") || apellido.length() > 50) {
            Toast.makeText(this, "Apellido inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fechaNacimiento.isEmpty() || !fechaNacimiento.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(this, "Fecha de nacimiento inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nacionalidad.isEmpty() || !nacionalidad.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "Nacionalidad inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (telefono.isEmpty() || !telefono.matches("\\d+") || telefono.length() != 10) {
            Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (correoElectronico.isEmpty() || !correoElectronico.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$")) {
            Toast.makeText(this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void modificarDatos() {
        String URL = "http://192.168.30.110:8080/Proyecto4to/modificarlocal.php";
        final String cedula = txtCedula.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String apellido = txtApellido.getText().toString().trim();
        final String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        final String nacionalidad = txtNacionalidad.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String correoElectronico = txtCorreoElectronico.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al actualizar datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
