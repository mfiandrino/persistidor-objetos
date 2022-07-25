package entities;

public class StructureChangedException extends RuntimeException {
  public StructureChangedException() {
    super("Se cambio la estructura");
  }
}
