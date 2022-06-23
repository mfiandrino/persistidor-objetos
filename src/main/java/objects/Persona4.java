package objects;

import entities.Persistable;
@Persistable
public class Persona4 {
  private String nombre;
  private Direccion direccion;


  public Persona4() {
  }

  public Persona4(String nombre, Direccion direccion) {
    this.nombre = nombre;
    this.direccion = direccion;
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
}
