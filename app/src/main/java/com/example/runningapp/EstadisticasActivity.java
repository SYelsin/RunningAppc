package com.example.runningapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import java.util.ArrayList;
import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        // Obtén una referencia al gráfico PieChart en tu diseño
        PieChart pieChart = findViewById(R.id.chart);

        // Configura el gráfico
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleRadius(58f);

        // Crea una lista de entradas de datos para el gráfico de pastel
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(45f, "10/03/2023"));
        pieEntries.add(new PieEntry(25f, "11/03/2023"));
        pieEntries.add(new PieEntry(10f, "12/03/2023"));
        pieEntries.add(new PieEntry(20f, "13/03/2023"));
        pieEntries.add(new PieEntry(30f, "14/03/2023"));

        // Crea un conjunto de datos para el gráfico de pastel
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Fecha");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(Color.parseColor("#FF9800"), Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"), Color.parseColor("#E91E63"),Color.parseColor("#800080"));

        // Crea un objeto PieData para agregar a tu gráfico
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        // Agrega el objeto PieData a tu gráfico
        pieChart.setData(pieData);

        // Actualiza tu gráfico
        pieChart.invalidate();
        // Calorias
        ArrayList<Integer> calorias = new ArrayList<>();
        calorias.add(1000);
        calorias.add(1200);
        calorias.add(900);
        calorias.add(1500);
        calorias.add(800);

        // Distancia
        ArrayList<Float> distancias = new ArrayList<>();
        distancias.add(5f);
        distancias.add(7f);
        distancias.add(4.5f);
        distancias.add(8f);
        distancias.add(3f);

        ArrayList<String> dias = new ArrayList<>();
        dias.add("10/03/2023");
        dias.add("11/03/2023");
        dias.add("12/03/2023");
        dias.add("13/03/2023");
        dias.add("14/03/2023");

        // Crea los puntos de datos para el gráfico
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < calorias.size(); i++) {
            entries.add(new Entry(i, calorias.get(i)));
        }

        // Crea los puntos de datos para el gráfico
        List<BarEntry> entriesb = new ArrayList<>();

        for (int i = 0; i < distancias.size(); i++) {
            entriesb.add(new BarEntry(i, distancias.get(i)));
        }

        // Configura el conjunto de datos de la línea
        LineDataSet dataSet = new LineDataSet(entries, "Calorías quemadas por día");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        // Configura el conjunto de datos de la barra
        BarDataSet dataSetb = new BarDataSet(entriesb, "Distancia recorrida por día");
        dataSet.setColor(Color.BLUE);

        // Crea el objeto LineData y configura el eje x
        LineData lineData = new LineData(dataSet);
        XAxis xAxis = ((LineChart) findViewById(R.id.lineChart)).getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dias));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Crea el objeto BarData y configura el eje x
        BarData barData = new BarData(dataSetb);
        XAxis xAxisb = ((BarChart) findViewById(R.id.barChart)).getXAxis();
        xAxisb.setValueFormatter(new IndexAxisValueFormatter(dias));
        xAxisb.setGranularity(1f);
        xAxisb.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Configura los ejes y la leyenda del gráfico
        YAxis yAxisLeft = ((LineChart) findViewById(R.id.lineChart)).getAxisLeft();
        yAxisLeft.setGranularity(1f);

        YAxis yAxisRight = ((LineChart) findViewById(R.id.lineChart)).getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = ((LineChart) findViewById(R.id.lineChart)).getLegend();
        legend.setEnabled(false);

        // Configura los ejes y la leyenda del gráfico
        YAxis yAxisLeftb = ((BarChart) findViewById(R.id.barChart)).getAxisLeft();
        yAxisLeftb.setGranularity(1f);

        YAxis yAxisRightb = ((BarChart) findViewById(R.id.barChart)).getAxisRight();
        yAxisRightb.setEnabled(false);

        Legend legendb = ((BarChart) findViewById(R.id.barChart)).getLegend();
        legendb.setEnabled(false);



        // Configura algunas propiedades adicionales del gráfico y establece el LineData
        ((LineChart) findViewById(R.id.lineChart)).setDescription(null);
        ((LineChart) findViewById(R.id.lineChart)).setData(lineData);
        ((LineChart) findViewById(R.id.lineChart)).invalidate();

        // Configura algunas propiedades adicionales del gráfico y establece el BarData
        ((BarChart) findViewById(R.id.barChart)).setDescription(null);
        ((BarChart) findViewById(R.id.barChart)).setData(barData);
        ((BarChart) findViewById(R.id.barChart)).setFitBars(true);
        ((BarChart) findViewById(R.id.barChart)).invalidate();

    }
}