package com.example.tppatrones.entities;

import com.example.tppatrones.Persistable;

public class Ordenador {
    @Persistable
    private int potencia;
    @Persistable
    private String nombre;
    @Persistable
    private Procesador CPU;
    public Procesador getCPU() {
        return CPU;
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

    public Ordenador(String nombre, int pot){
        this.setNombre(nombre);
        this.setPotencia(pot);
    }

    public String getNomPot(){
        return "Nombre"+this.getNombre()+" "+this.getPotencia();
    }

    public void setProcessor(Procesador CPU) {
        this.CPU = CPU;
    }

}
