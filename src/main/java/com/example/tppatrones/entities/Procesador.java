package com.example.tppatrones.entities;

import com.example.tppatrones.Persistable;

@Persistable
public class Procesador {
    private int nucleos;
    private String marca;

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }



    public int getNucleos() {
        return nucleos;
    }

    public void setNucleos(int nucleos) {
        this.nucleos = nucleos;
    }


}
