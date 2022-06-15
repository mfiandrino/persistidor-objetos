package persistentobject;

public class PersistentObject {

  public boolean store(long sId, Object o) {
    return true;
  }

  public <T> T load(long sId, Class<T> clazz) {
    return null;
  }

  public <T> boolean exists(long sId, Class<T> clazz) {
    return true;
  }

  public long elapsedTime(long sId) {
    return 1;
  }

  public <T> T delete(long sId, Class<T> clazz) {
    return null;
  }
}
