package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table
public class Attribute extends PersistentEntity {
  @Column
  private String name;
  @Column
  private String dataType;
  @Column
  private String value;

    //int id->objeto_persistido


  public Attribute() {
  }

  public Attribute(String name, String dataType, String value) {
    this.name = name;
    this.dataType = dataType;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getDataType() {
    return dataType;
  }

  public String getValue() {
    return value;
  }
}

