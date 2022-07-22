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
  @Column
  private Integer attributeObjectId;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name="attribute_id", nullable=false)
  private List<CollectionElement> collectionElements;

  //Creo que hay que agragarle un collectionElement_id con onToMany

  public Attribute() {
  }

  public Attribute(String name, String dataType, String value,Integer attributeObjectId) {
    this.name = name;
    this.dataType = dataType;
    this.value = value;
    this.attributeObjectId = attributeObjectId;
  }

  public List<CollectionElement> getCollectionElements() {
    return collectionElements;
  }

  public void setCollectionElements(List<CollectionElement> collectionElements) {
    this.collectionElements = collectionElements;
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

