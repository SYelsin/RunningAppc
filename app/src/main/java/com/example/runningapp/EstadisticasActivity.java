package com.example.runningapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        // Obtén una referencia al gráfico PieChart en tu diseño
        PieChart pieChart = findViewById(R.id.chart);

        // Obtén el ID del usuario actual
        datos myApp = (datos) getApplicationContext();
        String usuarioActual = myApp.getUsername();

        // Obtén los datos de Firebase y crea una lista de entradas de datos para el gráfico de pastel
        List<PieEntry> pieEntries = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("ejercicios").orderByChild("userId").equalTo(usuarioActual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    double calorias = snapshot.child("calorias").getValue(double.class);
                    String fecha = snapshot.child("fecha").getValue(String.class);
                    pieEntries.add(new PieEntry((float) calorias,fecha)); //La clave sería la fecha.
                }
                // Crea un conjunto de datos para el gráfico de pastel
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Fecha");
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setColors(Color.parseColor("#FF9800"), Color.parseColor("#2196F3"),
                        Color.parseColor("#4CAF50"), Color.parseColor("#E91E63"), Color.parseColor("#800080"));

                // Crea un objeto PieData para agregar a tu gráfico
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.YELLOW);
                pieChart.setNoDataText("Aun no hay estadisticas por mostrar");




                // Agrega el objeto PieData a tu gráfico
                pieChart.setData(pieData);

                // Actualiza tu gráfico
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores aquí
            }
        });


        BarChart barChart = findViewById(R.id.barChart);

        FirebaseDatabase.getInstance().getReference("ejercicios").orderByChild("userId").equalTo(usuarioActual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Float> totalDistances = new HashMap<>();
                List<String> xValues = new ArrayList<>(); // Agregamos una lista para almacenar las etiquetas del eje X
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.child("userId").getValue(String.class);
                    double distance = snapshot.child("distancia").getValue(double.class);
                    String dias = snapshot.child("fecha").getValue(String.class);
                    if (userId != null) {
                        totalDistances.put(dias, (float) distance);
                        xValues.add(dias); // Agregamos el valor de "dias" a la lista de etiquetas
                    }
                }
                // Crea una lista de entradas de datos para el gráfico de barras
                List<BarEntry> barEntries = new ArrayList<>();
                i = 0;
                for (Map.Entry<String, Float> entry : totalDistances.entrySet()) {
                    barEntries.add(new BarEntry(i++, entry.getValue()));
                }

                // Crea un conjunto de datos para el gráfico de barras
                BarDataSet barDataSet = new BarDataSet(barEntries, "Distancia (km)");
                barChart.setNoDataText("Aun no hay estadisticas por mostrar");

// Definir una paleta de colores dinámica
                barDataSet.setColors(Color.parseColor("#FF9800"), Color.parseColor("#2196F3"),
                        Color.parseColor("#4CAF50"), Color.parseColor("#E91E63"), Color.parseColor("#800080"));


                // Crea un objeto BarData para agregar a tu gráfico
                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.9f);

                // Agrega el objeto BarData a tu gráfico
                barChart.setData(barData);

                // Personaliza la apariencia del gráfico de barras
                barChart.setFitBars(true);
                barChart.getDescription().setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0f);
                barChart.getAxisRight().setAxisMinimum(0f);

                // Agrega las etiquetas del eje X
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

                // Actualiza tu gráfico
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores aquí
            }
        });

        LineChart lineChart = findViewById(R.id.lineChart);

// Obtén los datos de Firebase y crea una lista de entradas de datos para el gráfico de línea
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("ejercicios").orderByChild("userId").equalTo(myApp.getUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            double duracion = snapshot.child("tiempo").getValue(double.class);
                            String fecha = snapshot.child("fecha").getValue(String.class);
                            entries.add(new Entry(entries.size(),(float) duracion));
                            labels.add(fecha);
                        }

                        // Crea un conjunto de datos para el gráfico de línea
                        LineDataSet dataSet = new LineDataSet(entries, "Duración (hrs)");
                        dataSet.setColor(Color.RED);
                        dataSet.setLineWidth(2f);
                        dataSet.setCircleColor(Color.RED);
                        dataSet.setCircleRadius(5f);
                        dataSet.setDrawCircleHole(false);
                        dataSet.setValueTextSize(10f);
                        lineChart.setNoDataText("Aun no hay estadisticas por mostrar");


                        // Crea un objeto LineData para agregar a tu gráfico
                        LineData lineData = new LineData(dataSet);

                        // Agrega las etiquetas de día al eje X del gráfico

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int index = (int) value;
                                if (index >= 0 && index < labels.size()) {
                                    return labels.get(index);
                                }
                                return "";
                            }
                        });
                        xAxis.setGranularity(1f);

                        // Agrega el objeto LineData a tu gráfico
                        lineChart.setData(lineData);

                        // Actualiza tu gráfico
                        lineChart.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores aquí
                    }
                });






    }
}