package objects;

import entities.Persistable;

import java.util.ArrayList;
import java.util.List;

@Persistable
public class Casa {
  private String nombre;
  private ArrayList<Persona4> habitantes;

  public Casa() {
  }

  public Casa(String nombre, ArrayList<Persona4> habitantes) {
    this.nombre = nombre;
    this.habitantes = habitantes;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Persona4> getHabitantes() {
    return habitantes;
  }

  public void setHabitantes(ArrayList<Persona4> habitantes) {
    this.habitantes = habitantes;
  }
}
