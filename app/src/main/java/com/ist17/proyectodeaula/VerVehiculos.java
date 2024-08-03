package com.ist17.proyectodeaula;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class VerVehiculos extends AppCompatActivity {

    private static final String TAG = "VerVehiculos";
    EditText txtCedula;
    Button btnBuscar;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vehiculos);

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
                    buscarVehiculosPorCedula(cedula);
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese una cédula", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buscarVehiculosPorCedula(String cedula) {
        String URL = "http://10.10.28.113:8080/Proyecto4to/vervehiculos.php?cedula=" + cedula;

        // Realizar solicitud GET con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta recibida del servidor
                        mostrarVehiculos(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de Volley (por ejemplo, conexión fallida)
                        Log.e(TAG, "Error al buscar vehículos: ", error);  // Añadir un log del error
                        Toast.makeText(getApplicationContext(), "Error al buscar vehículos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Método para mostrar los datos de los vehículos en la tabla
    private void mostrarVehiculos(String response) {
        tableLayout.removeAllViews();

        // Procesar la respuesta recibida del servidor
        String[] vehiculos = response.split(";");

        if (vehiculos.length > 0 && !vehiculos[0].isEmpty()) {
            // Crear el encabezado de la tabla
            TableRow headerRow = new TableRow(this);

            TextView header = crearTextViewEncabezado("Datos del Vehículo");

            // Añadir encabezado a la fila de encabezado
            headerRow.addView(header);

            // Añadir la fila de encabezado a la tabla
            tableLayout.addView(headerRow);

            // Crear filas para cada vehículo
            for (String vehiculo : vehiculos) {
                String[] datosVehiculo = vehiculo.split(",");

                if (datosVehiculo.length >= 8) {
                    TableRow row = new TableRow(this);

                    // Crear un TextView con los datos del vehículo en una sola columna
                    TextView textView = crearTextView(datosVehiculo);

                    // Añadir el TextView a la fila
                    row.addView(textView);

                    // Agregar la fila a la tabla
                    tableLayout.addView(row);
                }
            }
        } else {
            // Mostrar mensaje si no se encontraron vehículos
            Toast.makeText(getApplicationContext(), "No se encontraron vehículos para ese ocupante", Toast.LENGTH_SHORT).show();
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
    private TextView crearTextView(String[] datosVehiculo) {
        StringBuilder texto = new StringBuilder();
        texto.append("ID Vehículo: ").append(datosVehiculo[0]).append("\n");
        texto.append("Marca: ").append(datosVehiculo[1]).append("\n");
        texto.append("Modelo: ").append(datosVehiculo[2]).append("\n");
        texto.append("Año: ").append(datosVehiculo[3]).append("\n");
        texto.append("Placa: ").append(datosVehiculo[4]).append("\n");
        texto.append("Color: ").append(datosVehiculo[5]).append("\n");
        texto.append("ID Ocupante: ").append(datosVehiculo[6]).append("\n");
        texto.append("Cédula: ").append(datosVehiculo[7]).append("\n");

        TextView textView = new TextView(this);
        textView.setText(texto.toString());
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.START);
        textView.setTextSize(12);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}
