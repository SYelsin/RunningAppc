package com.example.runningapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class CarreraActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String textoiniciar = "INICIAR";
    private String textoDetener = "DETENER";
    private Location mLastLocation;


    private MediaPlayer mediaPlayerStart;
    private MediaPlayer mediaPlayerStop;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location ultimaUbicacion;
    private double distanciaRecorrida = 0;
    private String tiempoActual;
    private String caloriasQuemadas;


    private int tiempo = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tiempo++;
            actualizarUI();
            handler.postDelayed(this, 1000);
        }
    };


    private TextView Tiempo, distancia, txtcalorias;
    private Button empezarBtn;
    private float distance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrera);

        Tiempo = findViewById(R.id.txtTiempo);
        distancia = findViewById(R.id.txtDistancia);
        txtcalorias = findViewById(R.id.txtCalorias);
        empezarBtn = findViewById(R.id.empezar_button);
        mediaPlayerStart = MediaPlayer.create(this, R.raw.iniciado);
        mediaPlayerStop = MediaPlayer.create(this, R.raw.finalizado);

        empezarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiempo == 0) {
                    handler.postDelayed(runnable, 1000);
                    empezarBtn.setText(textoDetener);
                    empezarBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.detener)));
                    mediaPlayerStart.start();
                } else {
                    handler.removeCallbacks(runnable);
                    actualizarUI();
                    empezarBtn.setText(textoiniciar);
                    empezarBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.iniciar)));
                    mediaPlayerStop.start();

                    // Crear un nuevo Intent
                    Intent intent = new Intent(CarreraActivity.this, ResumenActivity.class);
                    // Agregar los datos a enviar
                    intent.putExtra("Distancia", String.format("%.2f", distanciaRecorrida));
                    intent.putExtra("Duracion", tiempoActual);
                    intent.putExtra("Calorias", caloriasQuemadas);
                    // Iniciar el SegundoActivity
                    startActivity(intent);
                    finish();
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Obtener el proveedor de ubicación
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Solicitar los permisos de ubicación si no están activos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mLocationPermissionGranted = true;
        }
        solicitarHabilitarGPS();
    }

    private void actualizarUI() {
        int horas = tiempo / 3600;
        int minutos = (tiempo % 3600) / 60;
        int segundos = tiempo % 60;

        tiempoActual = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        Tiempo.setText(tiempoActual);

        contarDistancia();
        // Actualizar la distancia recorrida en la UI
        distancia.setText(String.format("%.2f", distanciaRecorrida));

        // Calcular las calorías quemadas

        //pasar tiempo
        String tiempo = tiempoActual; // tiempo en formato "horas:minutos:segundos"
        String[] tiempoSeparado = tiempo.split(":"); // dividir el tiempo en partes separadas

        int horas1 = Integer.parseInt(tiempoSeparado[0]); // obtener las horas
        int minutos1 = Integer.parseInt(tiempoSeparado[1]); // obtener los minutos
        int segundos1 = Integer.parseInt(tiempoSeparado[2]); // obtener los segundos

        int totalSegundos = (horas * 3600) + (minutos * 60) + segundos;



        float peso = 70; // Peso corporal en kilogramos
        double distancia = distanciaRecorrida; // Distancia recorrida en kilómetros
        double tiempoc = totalSegundos; // Tiempo de carrera en segundos (30 minutos)
        double velocidad = distancia / (tiempoc / 3600.0); // Velocidad en km/h
        double MET = 9.8 * velocidad / 3.5 + 3.5; // Intensidad del ejercicio en METs
        double duracion_horas = tiempoc / 3600.0;
        double calorias = ((0.75 * peso) + (MET * peso * duracion_horas)) / 2.0;
        double kcalorias = calorias / 1000.0; // Convertir a kilocalorías (kcal)
        caloriasQuemadas = String.format("%.2f", kcalorias);
        txtcalorias.setText(caloriasQuemadas);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Obtener la última ubicación conocida del usuario
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Si se encuentra una ubicación, centrar la cámara en ella
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        contarDistancia();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        }
    }
    private void solicitarHabilitarGPS() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("El GPS está desactivado, ¿desea habilitarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            finish();
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        }
    }

    private void contarDistancia() {
        // Verificar si la aplicación tiene permiso para acceder a la ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no hay permiso, solicitarlo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Si ya hay permiso, inicializar FusedLocationProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            // Obtener la última ubicación conocida
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    ultimaUbicacion = location;
                }
            });
            // Iniciar la actualización de ubicación en tiempo real
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000); // 1 segundo
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            // Se llama cada vez que la ubicación cambia
            for (Location location : locationResult.getLocations()) {
                if (ultimaUbicacion != null) {
                    // Añadir la distancia recorrida en metros desde la última ubicación a la distancia total
                    distance += ultimaUbicacion.distanceTo(location);
                    distanciaRecorrida = distance/1000;

                }
                ultimaUbicacion = location;
            }
        }
    };

    }



