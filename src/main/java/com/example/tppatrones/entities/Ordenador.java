package com.example.tppatrones.entities;

import com.example.tppatrones.Persistable;

import java.util.ArrayList;

public class Ordenador {
    @Persistable
    private int potencia;
    @Persistable
    private String nombre;
    @Persistable
    private Procesador CPU;


    @Persistable
    private ArrayList<String> listaDeMemorias;

    public Ordenador(String nombre, int pot){
        this.setNombre(nombre);
        this.setPotencia(pot);
    }

    public Procesador getCPU() {
        return CPU;
    }

    public ArrayList<String> getListaDeMemorias() {
        return listaDeMemorias;
    }

    public void setListaDeMemorias(ArrayList<String> listaDeMemorias) {
        this.listaDeMemorias = listaDeMemorias;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }


    public String getNomPot(){
        return "Nombre"+this.getNombre()+" "+this.getPotencia();
    }

    public void setProcessor(Procesador CPU) {
        this.CPU = CPU;
    }

}
