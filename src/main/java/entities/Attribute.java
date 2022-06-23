package entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
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

  //@OneToOne(cascade={CascadeType.ALL})
  //@JoinColumn(name="attribute_objets_id",referencedColumnName = "id");
  @Column
  private Integer attributeObjectId;

    //int id->objeto_persistido


  public Attribute() {
  }

  public Attribute(String name, String dataType, String value,Integer attributeObjectId) {
    this.name = name;
    this.dataType = dataType;
    this.value = value;
    this.attributeObjectId = attributeObjectId;
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

  public Integer getAttributeObjectId() {
    return attributeObjectId;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

