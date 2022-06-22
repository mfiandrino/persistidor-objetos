package persistentobject;

import db.EntityManagerHelper;
import entities.Attribute;
import entities.NotPersistable;
import entities.Persistable;
import entities.PersistedObject;
import objects.Direccion;

import javax.persistence.NoResultException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class PersistentObject {

  public boolean store(long sId, Object o) throws IllegalAccessException {

      System.out.println("No Existe y vamos a almacenar");
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
    String className = o.getClass().getCanonicalName();

    for (Field f : fields) {
      if((isObjectPersistible && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible && f.isAnnotationPresent(Persistable.class) )) {
        /*if(esObjeto) {
          id = store(1, objeto,fatherid)
          Attribute att = new Attribute(o.getName(),o.getType().toString(),null, objeto.id);
        }*/
        f.setAccessible(true);
        Attribute att = new Attribute(f.getName(),f.getType().toString().replace("class ", ""),f.get(o).toString());
        attributes.add(att);
      }
    }
    return new PersistedObject(sId, className, attributes);
  }

  public <T> T load(long sId, Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";
    PersistedObject object = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();

    Class<?> clazz2 = Class.forName(object.getClassName());
    Object instance = clazz2.newInstance();

    // Object instance = clazz.newInstance(); // Esto solo me parece que es suficiente
    List<Attribute> atts = object.getAttributes();

    for(Attribute att: atts) {
      //Attribute
      Class<?> attClazz = Class.forName(att.getDataType());
      Constructor<?> attConstructor = attClazz.getConstructor(String.class);
      Object attInstance = attConstructor.newInstance(att.getValue());

      //Setter
      String attName = att.getName();
      String attNameCapitalized = attName.substring(0,1).toUpperCase() + attName.substring(1);
      String setterName = "set" + attNameCapitalized;
      Method setter = clazz2.getDeclaredMethod(setterName, Class.forName(att.getDataType()));
      setter.invoke(instance, attInstance);
    }
    return (T) instance;
  }

  public <T> boolean exists(long sId, Class<T> clazz) {
    String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";
    try {
      PersistedObject object = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();
    } catch (NoResultException e) {
      return false;
    }
    return true;
  }

  public long elapsedTime(long sId) {
    return 1;
  }

  public <T> T delete(long sId, Class<T> clazz) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    T object = null;
    if (exists(sId, clazz)) {
      object = load(sId,clazz);
      String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";
      PersistedObject persistedObject = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();
      EntityManagerHelper.beginTransaction();
      EntityManagerHelper.getEntityManager().remove(persistedObject);
      EntityManagerHelper.commit();
    }
    return object;
  }
}
