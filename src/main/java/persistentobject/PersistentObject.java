package persistentobject;

import db.EntityManagerHelper;
import entities.Attribute;
import entities.NotPersistable;
import entities.Persistable;
import entities.PersistedObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PersistentObject {

  public boolean store(long sId, Object o) throws IllegalAccessException {
    PersistedObject perObj = crearPersistedObject(sId,o);
    EntityManagerHelper.beginTransaction();
    EntityManagerHelper.getEntityManager().persist(perObj);
    EntityManagerHelper.commit();
    return true;
  }

  private PersistedObject crearPersistedObject(long sId, Object o) throws IllegalAccessException {
    List<Attribute> attributes = new ArrayList<>();

    Boolean isObjectPersistible = o.getClass().isAnnotationPresent(Persistable.class);
    Field[] fields = o.getClass().getDeclaredFields();
    String className = o.getClass().getSimpleName();

    for (Field f : fields) {
      if((isObjectPersistible && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible && f.isAnnotationPresent(Persistable.class) )) {
        f.setAccessible(true);
        Attribute att = new Attribute(f.getName(),f.getType().toString(),f.get(o).toString());
        attributes.add(att);
      }
    }

    return new PersistedObject(sId, className, attributes);
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
