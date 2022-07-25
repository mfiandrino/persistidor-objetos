package objects;

import entities.Persistable;

import java.util.ArrayList;

@Persistable
public class Persona5 {
  private String nombre;
  private Direccion direccion;
  private ArrayList<Direccion> direcciones;
  private ArrayList<String> telefonos;


  public Persona5() {
  }

  public Persona5(String nombre, Direccion direccion, ArrayList<Direccion> direcciones, ArrayList<String> telefonos) {
    this.nombre = nombre;
    this.direccion = direccion;
    this.direcciones = direcciones;
    this.telefonos = telefonos;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }

  public ArrayList<Direccion> getDirecciones() {
    return direcciones;
  }

  public void setDirecciones(ArrayList<Direccion> direcciones) {
    this.direcciones = direcciones;
  }

  public ArrayList<String> getTelefonos() {
    return telefonos;
  }

  public void setTelefonos(ArrayList<String> telefonos) {
    this.telefonos = telefonos;
  }
}
