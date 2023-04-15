package com.example.runningapp.clases;

public class User {
    private String name;
    private String fotoperfil;
    private String distancia;
    private String calorias;
    private String username;
    private int seguidores;
    private int seguidos;
    public User() {}

    public User(String username, String name, String fotoperfil,String distancia, String calorias, int seguidores, int seguidos) {
        this.username = username;
        this.name = name;
        this.fotoperfil = fotoperfil;
        this.distancia = distancia;
        this.calorias = calorias;
        this.seguidores = seguidores;
        this.seguidos = seguidos;

    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getSeguidos() {
        return seguidos;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }

    public String getDistancia() {
        return distancia;
    }

    public String getCalorias() {
        return calorias;
    }

    public int getSeguidores() {
        return seguidores;
    }
}
