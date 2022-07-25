package objects;

import entities.Persistable;

import java.util.ArrayList;
import java.util.List;
@Persistable
public class CasaPersonas5 {
  private String nombre;
  private ArrayList<Persona5> habitantes;

  public CasaPersonas5() {
  }

  public CasaPersonas5(String nombre, ArrayList<Persona5> habitantes) {
    this.nombre = nombre;
    this.habitantes = habitantes;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Persona5> getHabitantes() {
    return habitantes;
  }

  public void setHabitantes(ArrayList<Persona5> habitantes) {
    this.habitantes = habitantes;
  }
}