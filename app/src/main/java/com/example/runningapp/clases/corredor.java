package com.example.runningapp.clases;

public class corredor implements Comparable<corredor> {

    public String name;
    public String distancia;
    public String calorias;
    public String tiempo;
    public String fotoperfil;
    public String posicionTexto;


    public corredor() {
    }

    public corredor(String name, String distancia, String calorias, String tiempo, String fotoperfil) {
        this.name = name;
        this.distancia = distancia;
        this.calorias = calorias;
        this.fotoperfil = fotoperfil;
        this.tiempo = tiempo;
    }

    // Método para comparar corredores por distancia (y calorias en caso de empate)
    /*@Override
    public int compareTo(corredor otroCorredor) {
        if (this.distancia != otroCorredor.distancia) {
            return Integer.compare(this.distancia, otroCorredor.distancia);
        } else {
            return Integer.compare(this.calorias, otroCorredor.calorias);
        }
    }*/

    // Método para comparar corredores por distancia (y calorias en caso de empate)
    @Override
    public int compareTo(corredor otroCorredor) {
        if (!this.calorias.equals(otroCorredor.calorias)) {
            return Double.compare(Double.parseDouble(otroCorredor.calorias), Double.parseDouble(this.calorias));
        } else {
            return Double.compare(Double.parseDouble(this.distancia), Double.parseDouble(otroCorredor.distancia));
        }
    }



}

