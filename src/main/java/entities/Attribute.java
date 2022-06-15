package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Attribute extends PersistentEntity {
  @Column
  private String name;
  @Column
  private String dataType;
  @Column
  private String value;

  public Attribute(String name, String dataType, String value) {
    this.name = name;
    this.dataType = dataType;
    this.value = value;
  }
}

