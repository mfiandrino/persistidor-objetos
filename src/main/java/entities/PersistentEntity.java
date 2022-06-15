package entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentEntity {
  @Id
  @GeneratedValue
  private int id;

  public int getId() {
    return id;
  }
}
