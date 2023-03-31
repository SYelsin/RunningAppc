package com.example.runningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ResumenActivity extends AppCompatActivity {

    private TextView txtDuracion, txtDistancia, txtCalorias;
    private Button homeBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        ImageView imageView = findViewById(R.id.imageGif);
        Glide.with(this).asGif().load(R.drawable.premio).into(imageView);

        txtDuracion = findViewById(R.id.txtDuracionr);
        txtDistancia = findViewById(R.id.txtDistanciar);
        txtCalorias = findViewById(R.id.txtKaloriasr);
        homeBoton = findViewById(R.id.homeBoton);

        // Recuperar los datos enviados desde el PrimerActivity
        Intent intent = getIntent();
        String duracion = intent.getStringExtra("Duracion");
        String distancia = intent.getStringExtra("Distancia");
        String calorias = intent.getStringExtra("Calorias");
        // Mostrar los datos en la pantalla
        txtDuracion.setText(duracion);
        txtDistancia.setText(distancia + " km");
        txtCalorias.setText(calorias + " kcal");

        homeBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}