package com.example.ant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class CamaraFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1000;

    private Button fotoblanca, fotoAzul, guardar;
    private ImageView fotopreviw1, Fotopreviw2;
    private static final int REQUEST_TAKE_PHOTO_BLANCA = 1;
    private static final int REQUEST_TAKE_PHOTO_AZUL = 2;
    private String encodedImageBlanca, encodedImageAzul;

    public CamaraFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_camara, container, false);

        fotoblanca = vista.findViewById(R.id.fotoblanca);
        fotoAzul = vista.findViewById(R.id.fotoazul);
        fotopreviw1 = vista.findViewById(R.id.imageView3);
        Fotopreviw2 = vista.findViewById(R.id.imageView4);
        guardar = vista.findViewById(R.id.button4);

        // Verificar y solicitar permisos antes de tomar una foto
        checkAndRequestPermissions();

                // Botón CAPTURAR FOTO BLANCA
                fotoblanca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tomarFoto(REQUEST_TAKE_PHOTO_BLANCA);
                    }
                });

                // Botón CAPTURAR FOTO AZUL
                fotoAzul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tomarFoto(REQUEST_TAKE_PHOTO_AZUL);
                    }
                });

                // Botón GUARDAR (Enviar imágenes al servidor)
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Verifica que ambas imágenes han sido capturadas antes de intentar subirlas
                        if (encodedImageBlanca != null && encodedImageAzul != null) {
                            subirImagenes("https://proyectofinalaa.000webhostapp.com/ant/instertarimagen.php", encodedImageBlanca, encodedImageAzul);
                            Toast.makeText(requireContext(), "Imágenes guardadas correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            // Manejar caso donde una o ambas imágenes no han sido capturadas
                            Toast.makeText(requireContext(), "Error: Una o ambas imágenes no han sido capturadas", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                return vista;
    }
    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void tomarFoto(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private String convertirImagenBase64(Bitmap bitmap) {
        int targetWidth = 800; // El ancho deseado para la imagen redimensionada
        int targetHeight = (int) (bitmap.getHeight() * (targetWidth / (double) bitmap.getWidth()));

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }



    private void subirImagenes(String url, final String fotoblanca, final String fotoazul) {
        // Instanciar la cola de solicitudes (RequestQueue).
        RequestQueue colaSolicitudes = Volley.newRequestQueue(requireContext());

        // Crear un objeto JSON para almacenar las imágenes
        JSONObject jsonImages = new JSONObject();
        try {
            jsonImages.put("fotoblanca_base64", fotoblanca);
            jsonImages.put("fotoazul_base64", fotoazul);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud JSON POST
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, url, jsonImages,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor JSON
                        try {
                            // Aquí puedes procesar la respuesta JSON
                            if (response.has("status")) {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    // La operación fue exitosa
                                    Toast.makeText(requireContext(), "Foto enviada correctamente", Toast.LENGTH_SHORT).show();
                                    // Limpia las variables y las ImageView después de subir las imágenes
                                    encodedImageBlanca = null;
                                    encodedImageAzul = null;

                                    // Añade logs para verificar si se ejecuta este bloque
                                    Log.d("LimpiarImageViews", "Limpiando las ImageView después de subir las imágenes");
                                    fotopreviw1.setImageDrawable(null);
                                    Fotopreviw2.setImageDrawable(null);

                                    // Puedes mostrar un mensaje, actualizar la UI, etc.
                                } else {
                                    // La operación no fue exitosa
                                    Toast.makeText(requireContext(), "Foto NO enviada correctamente", Toast.LENGTH_SHORT).show();

                                    // Puedes manejar el error, mostrar un mensaje de error, etc.
                                }
                            } else {
                                // La respuesta no contiene un campo 'status'
                                Toast.makeText(requireContext(), "NO enviada correctamente", Toast.LENGTH_SHORT).show();

                                // Puedes manejar esto según tus necesidades
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Manejar error al procesar la respuesta JSON
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                    }
                });

        // Agregar la solicitud a la cola de solicitudes.
        colaSolicitudes.add(solicitud);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = null;

                // Obtener la imagen del intent
                if (requestCode == REQUEST_TAKE_PHOTO_BLANCA || requestCode == REQUEST_TAKE_PHOTO_AZUL) {
                    Bundle extras = data.getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        imageBitmap = (Bitmap) extras.get("data");
                    }
                }

                // Verificar si la imagen se capturó correctamente
                if (imageBitmap != null) {
                    // Convertir la imagen a base64
                    String encodedImage = convertirImagenBase64(imageBitmap);

                    // Verificar si la cadena base64 se generó correctamente
                    if (encodedImage != null) {
                        // Asignar la cadena base64 a la variable correspondiente
                        if (requestCode == REQUEST_TAKE_PHOTO_BLANCA) {
                            encodedImageBlanca = encodedImage;
                            fotopreviw1.setImageBitmap(imageBitmap);
                        } else if (requestCode == REQUEST_TAKE_PHOTO_AZUL) {
                            encodedImageAzul = encodedImage;
                            Fotopreviw2.setImageBitmap(imageBitmap);
                        } else {
                            // Manejar caso de requestCode no reconocido
                        }
                    } else {
                        // Manejar caso de encodedImage nulo
                        Toast.makeText(requireContext(), "Error al convertir la imagen a base64", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar caso de imagen nula
                    Toast.makeText(requireContext(), "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}