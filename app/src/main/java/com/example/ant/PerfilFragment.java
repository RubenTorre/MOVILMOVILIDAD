package com.example.ant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    //variables propias
    TextView nombre,apellido;
    Button InfoPersonal,Eliminar,Logaut;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_perfil, container, false);

        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        nombre = (TextView) vista.findViewById(R.id.nombreusu);
        apellido = (TextView) vista.findViewById(R.id.apellidousu);

        InfoPersonal = (Button) vista.findViewById(R.id.info);
        Eliminar = (Button) vista.findViewById(R.id.btndirecion);
        Logaut = (Button) vista.findViewById(R.id.logaut);


        // Recuperar el nombre de usuario desde SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = preferences.getString("nombre", "");
        String apellidoUsuario = preferences.getString("apellido", "");
        String numeroidentifiacion = preferences.getString("numeroidentifiacion","");


        // Mostrar nombre y apellido en TextView
        nombre.setText(nombreUsuario);
        apellido.setText( apellidoUsuario);


//Boton Informacion Personal
        InfoPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un diálogo de confirmación con la información del usuario

                // Construir un mensaje HTML con la información del usuario
                // Construir el mensaje del cuadro de diálogo con la información del usuario en forma de tabla
                String message = "Nombre: " + nombreUsuario + "\n" +
                        "Apellido: " + apellidoUsuario + "\n" +
                        "Número de Identificación: " + numeroidentifiacion;

// Mostrar un diálogo de confirmación con la información del usuario en forma de texto
                new AlertDialog.Builder(getContext())
                        .setTitle("Información del Usuario")
                        .setMessage(message)
                        .setPositiveButton("Aceptar", null)
                        .show();

            }
        });
//bolo eliminar Cuenta
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar Cuenta")
                        .setMessage("Solo el administrador puede eliminar la cuenta")
                        .setPositiveButton("Aceptar", null)  // No se especifica un OnClickListener para Aceptar
                        .show();

            }
        });




//boton cerrar seesion
        Logaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un diálogo de confirmación
                new AlertDialog.Builder(getContext())
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Está seguro de que desea cerrar sesión?")
                        .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Borrar cualquier información de inicio de sesión almacenada localmente
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear(); // Eliminar todas las preferencias almacenadas
                                editor.apply();

                                // Redirigir al primer activity (inicio de sesión)
                                // Obtener el contexto del fragmento
                                Context context = getContext(); // O también puedes usar requireContext();

                                // Crear el Intent utilizando el contexto obtenido
                                Intent intent = new Intent(context, MainActivity.class);

                                // Limpiar la pila de actividades y comenzar la nueva actividad
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });


        return vista;




    }


}