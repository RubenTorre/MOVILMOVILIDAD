package com.example.ant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //Timer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Conexion/Enlace A Scren o layout
                Intent pantallalogin = new Intent(MainActivity.this,MainActivity2.class );
                startActivity(pantallalogin);
                finish();



            }
        },3000);


    }
}