package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class PersistedObject extends PersistentEntity {

  @Column
  private Long ssId;
  @Column
  private String className;
  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name="object_id", nullable=false)
  private List<Attribute> attributes;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name="attribute_id", nullable=false)
  private List<CollectionElement> collectionElements;

  public PersistedObject() {
    //this.collectionElements = new ArrayList<>();
  }

  public PersistedObject(Long ssId, String className, List<Attribute> attributes) {
    this.ssId = ssId;
    this.className = className;
    this.attributes = attributes;
    this.collectionElements = new ArrayList<>();
  }

  public List<CollectionElement> getCollectionElements() {
    return collectionElements;
  }

  public void setCollectionElements(List<CollectionElement> collectionElements) {
    this.collectionElements = collectionElements;
  }

  public Long getSsId() {
    return ssId;
  }

  public String getClassName() {
    return className;
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }
}
