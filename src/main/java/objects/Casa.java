package objects;

import entities.Persistable;

@Persistable
public class Casa {
  private Persona4 persona4;

  public Casa() {
  }

  public Casa(Persona4 persona4) {
    this.persona4 = persona4;
  }

  public Persona4 getPersona4() {
    return persona4;
  }

  public void setPersona4(Persona4 persona4) {
    this.persona4 = persona4;
  }
}
