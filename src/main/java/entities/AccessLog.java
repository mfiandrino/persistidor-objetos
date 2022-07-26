package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
public class AccessLog extends PersistentEntity{
  @Column
  private long ssId;
  @Column
  private LocalDateTime lastAccess;

  public long getSsId() {
    return ssId;
  }

  public void setSsId(long ssId) {
    this.ssId = ssId;
  }

  public AccessLog() {
  }

  public AccessLog(long ssId, LocalDateTime lastAccess){
    this.ssId=ssId;
    this.lastAccess=lastAccess;
  }

  public LocalDateTime getLastAccess() {
    return lastAccess;
  }

  public void setLastAccess(LocalDateTime lastAccess) {
    this.lastAccess = lastAccess;
  }
}
