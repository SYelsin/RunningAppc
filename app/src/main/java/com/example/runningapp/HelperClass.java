package com.example.runningapp;

public class HelperClass {
    private String name, email, username, password,fotoperfil, pais, peso,altura,distancia;
    String tiempo,ritmo;

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    private String calorias;
    private int pasos;

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getRitmo() {
        return ritmo;
    }

    public void setRitmo(String ritmo) {
        this.ritmo = ritmo;
    }

    public int getPasos() {
        return pasos;
    }

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }


    public HelperClass(String name, String email, String username, String password, String fotoperfil, String pais, String peso, String altura, String tiempo, String ritmo, String calorias, String distancia, int pasos) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fotoperfil = fotoperfil;
        this.pais = pais;
        this.peso = peso;
        this.altura = altura;
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

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

}
