package com.example.runningapp;

public class HelperClass {
    private String name, email, username, password;
    int tiempo,ritmo;
    private double calorias, distancia;
    private int pasos;

    public int getRitmo() {
        return ritmo;
    }

    public void setRitmo(int ritmo) {
        this.ritmo = ritmo;
    }

    public int getPasos() {
        return pasos;
    }

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }

    public HelperClass(String name, String email, String username, String password, int tiempo, int ritmo, double calorias, double distancia, int pasos) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.tiempo = tiempo;
        this.ritmo = ritmo;
        this.calorias = calorias;
        this.distancia = distancia;
        this.pasos = pasos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

}
