package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class PersistedObject extends PersistentEntity {
  @Column
  private int ssId;
  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name="object_id", nullable=false)
  private List<Attribute> attributes;

  public PersistedObject(int ssId, List<Attribute> attributes) {
    this.ssId = ssId;
    this.attributes = attributes;
  }


}
