package com.example.runningapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalentamientoActivity2 extends AppCompatActivity {
    private Button botonContinuar, botonTiempo;
    private TextView ejer;
    private int tiempoRestante;
    private CountDownTimer temporizador;

    private MediaPlayer mediaPlayerStart,mediaPlayerStop,ejerc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calentamiento2);

        botonContinuar = findViewById(R.id.btnContinuar2);
        botonContinuar.setVisibility(View.GONE);

        botonTiempo = findViewById(R.id.textTiempo2);
        botonTiempo.setText("Empezar");
        botonTiempo.setVisibility(View.GONE);

        ejer = findViewById(R.id.txtejer2);

        mediaPlayerStart = MediaPlayer.create(this, R.raw.iniciado);
        mediaPlayerStop = MediaPlayer.create(this, R.raw.finalizado);
        ejerc = MediaPlayer.create(this,R.raw.estiramientos);
        ejerc.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                botonTiempo.setVisibility(View.VISIBLE);
                ejer.setVisibility(View.GONE);
                botonContinuar.setVisibility(View.VISIBLE);
            }
        });
        ejerc.start();

        temporizador = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) millisUntilFinished / 1000;
                int minutos = tiempoRestante / 60;
                int segundos = tiempoRestante % 60;
                botonTiempo.setText(String.format("%02d:%02d", minutos, segundos));
            }

            @Override
            public void onFinish() {
                mediaPlayerStop.start();
                botonTiempo.setVisibility(View.GONE);
            }
        };

        botonTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerStart.start();
                temporizador.start();
                botonTiempo.setEnabled(false);
            }
        });

        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalentamientoActivity2.this, CalentamientoActivity3.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        temporizador.cancel();
    }
}
