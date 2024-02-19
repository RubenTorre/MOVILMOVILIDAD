package com.example.ant;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {


    //Variables Propias
    ImageView logo;
    EditText TxtUser, TxtPass;
    Button BtnValidar;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //Logica vs Grafica
        logo = (ImageView) findViewById(R.id.imageView2);
        TxtUser = (EditText) findViewById(R.id.editTextTextPersonName);
        TxtPass = (EditText) findViewById(R.id.editTextTextPassword);
        BtnValidar = (Button) findViewById(R.id.button);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);


        // BOTON DE LOGIN
        BtnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar campos vacíos
                if (TxtUser.getText().toString().isEmpty() || TxtPass.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Existen campos vacíos", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar el ProgressBar antes de hacer la solicitud
                    progressBar.setVisibility(View.VISIBLE);
                    // Oculta los elementos al hacer clic en el botón
                    ocultarElementos();


                    // Validar credenciales
                    validarCredenciales("https://proyectofinalaa.000webhostapp.com/ant/validar_usuario.php");
                }
            }

        });
    }

    private void validarCredenciales(String URL) {
        Log.d("MainActivity2", "Presionaste el botón. Datos: " + TxtUser.getText().toString() + " - " + TxtPass.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("MainActivity2", "Respuesta del servidor: " + response);

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("error")) {
                                // Se encontró un error en la respuesta del servidor
                                String errorMessage = jsonObject.getString("error");
                                Log.e("MainActivity2", "Error en la respuesta del servidor: " + errorMessage);
                                Toast.makeText(MainActivity2.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Procesar la respuesta exitosa
                                // Obtener datos del agente
                                int idAgente = jsonObject.getInt("idagente");
                                String nombre = jsonObject.getString("nombre");
                                String apellido = jsonObject.getString("apellido");
                                String numeroIdentificacion = jsonObject.getString("numeroidentifiacion");
                                int nivel = jsonObject.getInt("nivel");

                                // Crear un Intent para pasar a MainActivity3
                                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);

                                // Guardar información del agente en SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("idagente", idAgente);
                                editor.putString("nombre", nombre);
                                editor.putString("apellido", apellido);
                                editor.putString("numeroidentifiacion", numeroIdentificacion);
                                editor.putInt("nivel", nivel);
                                editor.apply();

                                // Iniciar MainActivity3
                                startActivity(intent);
                            }
                            // Después de procesar la respuesta, oculta la barra de progreso
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity2.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                            // En caso de error, oculta la barra de progreso
                            progressBar.setVisibility(View.GONE);
                            mostrarElementos();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            // Error de solicitud incorrecta (400)
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("MainActivity2", "Error 400 - Respuesta del servidor: " + responseBody);
                            Toast.makeText(MainActivity2.this, responseBody, Toast.LENGTH_SHORT).show();
                        } else {
                            // Otro tipo de error
                            Toast.makeText(MainActivity2.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();

                        }
                        // En caso de error, oculta la barra de progreso
                        progressBar.setVisibility(View.GONE);
                        // Muestra los elementos nuevamente
                        mostrarElementos();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // Construir el cuerpo de la solicitud en formato JSON
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("nombre", TxtUser.getText().toString());
                    jsonBody.put("numeroidentifiacion", TxtPass.getText().toString());
                    final String requestBody = jsonBody.toString();

                    // Convertir el cuerpo a bytes
                    return requestBody.getBytes("utf-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        requestQueue.add(stringRequest);
    }
    private void ocultarElementos() {
        // Oculta los EditText y el botón
        TxtUser.setVisibility(View.GONE);
        TxtPass.setVisibility(View.GONE);
        BtnValidar.setVisibility(View.GONE);
    }

    private void mostrarElementos() {
        // Muestra los EditText y el botón
        TxtUser.setVisibility(View.VISIBLE);
        TxtPass.setVisibility(View.VISIBLE);
        BtnValidar.setVisibility(View.VISIBLE);
    }
}