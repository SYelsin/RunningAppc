package com.example.runningapp.clases;

public class Ejercicio {
    private String userId;
    private String fecha;
    private Double distancia;
    private Double calorias;
    private Double tiempo;

    public Ejercicio() {}

    public Ejercicio(String userId, String fecha, double distancia, double calorias, double tiempo) {
        this.userId = userId;
        this.fecha = fecha;
        this.distancia = distancia;
        this.calorias = calorias;
        this.tiempo = tiempo;
    }

    public String getUserId() {
        return userId;
    }

    public String getFecha() {
        return fecha;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getCalorias() {
        return calorias;
    }

    public double getTiempo() {
        return tiempo;
    }
}
