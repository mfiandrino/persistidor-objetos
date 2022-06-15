package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class PersistedObject extends PersistentEntity {
  @Column
  private long ssId;
  @Column
  private String className;
  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name="object_id", nullable=false)
  private List<Attribute> attributes;

  public PersistedObject(long ssId, String className, List<Attribute> attributes) {
    this.ssId = ssId;
    this.className = className;
    this.attributes = attributes;
  }
}
