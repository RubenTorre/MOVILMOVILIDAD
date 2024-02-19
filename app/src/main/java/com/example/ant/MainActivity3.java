package com.example.ant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity3 extends AppCompatActivity {

    //Variables Propias
    MeowBottomNavigation bottomNavigation;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //Asociar parte Grafica vs Logica
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottomNavigation);
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);



        //Asociar los iconos al bottonnavegation
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_article_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_camara));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_perfil));


        //Iniciar el fragment home
        bottomNavigation.show(1, true);

        //evento mostrar fragmentes (al hacer clic en el curve navigation)
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                if(model.getId()==1)
                {
                    fragment= new HomeFragment();
                }
                if(model.getId()==2)
                {
                    fragment= new formularioFragment();
                }
                if(model.getId()==3)
                {
                    fragment= new CamaraFragment();
                }

                if(model.getId()==4)
                {
                        fragment= new PerfilFragment();
                    // Obtener datos de SharedPreferences
                    int idAgente = preferences.getInt("idagente", 0);
                    String nombre = preferences.getString("nombre", "");
                    String apellido = preferences.getString("apellido", "");
                    String numeroidentifiacion = preferences.getString("numeroidentifiacion","");


                    // Agregar datos al Bundle
                    bundle.putInt("idAgente", idAgente);
                    bundle.putString("nombre", nombre);
                    bundle.putString("apellido", apellido);
                    bundle.putString("numeroidentifiacion",numeroidentifiacion);

                    // Pasar el Bundle al Fragment antes de cargarlo
                    if (fragment != null) {
                        fragment.setArguments(bundle);
                        cargarFragment(fragment);
                    }
                }

                cargarFragment(fragment);
                return null;
            }
        });



    }

    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container, fragment, null)
                .commit();
    }
}