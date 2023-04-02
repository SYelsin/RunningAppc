package com.example.runningapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalentamientoActivity1 extends AppCompatActivity {
    private Button botonContinuar, botonTiempo;
    private TextView ejer;
    private int tiempoRestante;
    private CountDownTimer temporizador;

    private MediaPlayer mediaPlayerStart,mediaPlayerStop,caminata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calentamiento1);

        botonContinuar = findViewById(R.id.btnContinuar);
        botonContinuar.setVisibility(View.GONE);

        botonTiempo = findViewById(R.id.textTiempo);
        botonTiempo.setText("Empezar");
        botonTiempo.setVisibility(View.GONE);

        ejer = findViewById(R.id.txtejer);

        mediaPlayerStart = MediaPlayer.create(this, R.raw.iniciado);
        mediaPlayerStop = MediaPlayer.create(this, R.raw.finalizado);
        caminata = MediaPlayer.create(this,R.raw.caminata);
        caminata.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                botonTiempo.setVisibility(View.VISIBLE);
                ejer.setVisibility(View.GONE);
                botonContinuar.setVisibility(View.VISIBLE);
            }
        });
        caminata.start();

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
                Intent intent = new Intent(CalentamientoActivity1.this, CalentamientoActivity2.class);
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
