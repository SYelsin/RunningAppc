package com.example.runningapp;

public class DataClassu {
    private String name;
    private String email;
    private String username;
    private String password;
    private String peso;
    private String altura;
    private String fotoperfil;
    private String key;


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

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataClassu(String name, String email, String username, String password, String peso, String altura, String fotoperfil) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.peso = peso;
        this.altura = altura;
        this.fotoperfil = fotoperfil;
    }

    public DataClassu(){
    }
}
