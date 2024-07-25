package com.ist17.proyectodeaula;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VerPiloto extends AppCompatActivity {

    TextView txtCedula;
    Button btnBuscar;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_piloto);

        // Inicializar vistas
        txtCedula = findViewById(R.id.edtCedula);
        btnBuscar = findViewById(R.id.btnBuscar);
        tableLayout = findViewById(R.id.tableLayout);

        // Configurar listener para el botón de buscar
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cedula = txtCedula.getText().toString().trim();
                if (!cedula.isEmpty()) {
                    buscarParticipantePorCedula(cedula);
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese una cédula", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para buscar un participante por cédula
    private void buscarParticipantePorCedula(String cedula) {
        String URL = "http://192.168.30.110:8080/Proyecto4to/verusuarioslocal.php?cedula=" + cedula;

        // Realizar solicitud GET con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta recibida del servidor
                        mostrarParticipantes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de Volley (por ejemplo, conexión fallida)
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al buscar participante: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Método para mostrar los datos de los participantes en la tabla
    private void mostrarParticipantes(String response) {
        tableLayout.removeAllViews();

        // Procesar la respuesta recibida del servidor
        String[] participantes = response.split(";");

        if (participantes.length > 0) {
            // Crear el encabezado de la tabla
            TableRow headerRow = new TableRow(this);

            TextView cedulaHeader = crearTextViewEncabezado("Céd");
            TextView nombreHeader = crearTextViewEncabezado("Nomb");
            TextView apellidoHeader = crearTextViewEncabezado("Ape");
            TextView fechaNacimientoHeader = crearTextViewEncabezado("FDN");
            TextView nacionalidadHeader = crearTextViewEncabezado("Naci");
            TextView telefonoHeader = crearTextViewEncabezado("Telf");

            // Añadir encabezados a la fila de encabezado
            headerRow.addView(cedulaHeader);
            headerRow.addView(nombreHeader);
            headerRow.addView(apellidoHeader);
            headerRow.addView(fechaNacimientoHeader);
            headerRow.addView(nacionalidadHeader);
            headerRow.addView(telefonoHeader);

            // Añadir la fila de encabezado a la tabla
            tableLayout.addView(headerRow);

            // Crear filas para cada participante
            for (String participante : participantes) {
                String[] datosParticipante = participante.split(",");

                if (datosParticipante.length >= 6) {
                    TableRow row = new TableRow(this);

                    // Añadir cada campo de datos a la fila
                    for (int i = 0; i < datosParticipante.length; i++) {
                        TextView textView = crearTextView(datosParticipante[i]);
                        row.addView(textView);
                    }

                    // Agregar la fila a la tabla
                    tableLayout.addView(row);
                }
            }
        } else {
            // Mostrar mensaje si no se encontraron participantes
            Toast.makeText(getApplicationContext(), "No se encontraron participantes con esa cédula", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para crear TextView para el encabezado de la tabla
    private TextView crearTextViewEncabezado(String texto) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setBackgroundColor(Color.parseColor("#f0f0f0")); // Color de fondo gris claro
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    // Método para crear TextView para los datos de la tabla
    private TextView crearTextView(String texto) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setBackgroundColor(Color.WHITE); // Color de fondo blanco para los datos
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}
