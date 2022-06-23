package persistentobject;

import db.EntityManagerHelper;
import entities.Attribute;
import entities.NotPersistable;
import entities.Persistable;
import entities.PersistedObject;

import javax.persistence.NoResultException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentObject {

  private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_MAP;
  static {
    WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(16);
    WRAPPER_TYPE_MAP.put(Integer.class, int.class);
    WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
    WRAPPER_TYPE_MAP.put(Character.class, char.class);
    WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
    WRAPPER_TYPE_MAP.put(Double.class, double.class);
    WRAPPER_TYPE_MAP.put(Float.class, float.class);
    WRAPPER_TYPE_MAP.put(Long.class, long.class);
    WRAPPER_TYPE_MAP.put(Short.class, short.class);
    WRAPPER_TYPE_MAP.put(Void.class, void.class);
  }

  public boolean store(long sId, Object o) throws IllegalAccessException {
    /*if (exists(sId, o.getClass())) {
      System.out.println("Existe y vamos a actualizar");
      String hql = "from PersistedObject where ssId = " + sId + " and className = '" + o.getClass().getName() + "'";
      PersistedObject object = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();
      PersistedObject perObj = crearPersistedObject(sId,o);
      object.setAttributes(perObj.getAttributes());
      EntityManagerHelper.beginTransaction();
      EntityManagerHelper.getEntityManager().merge(object);
      EntityManagerHelper.commit();
      return true;
    }
    else {*/
      System.out.println("No Existe y vamos a almacenar");
      PersistedObject perObj = crearPersistedObject(sId, o);
      EntityManagerHelper.beginTransaction();
      EntityManagerHelper.getEntityManager().persist(perObj);
      EntityManagerHelper.commit();
      return false;
    //}
  }

  /*private PersistedObject updateObject(PersistedObject viejo, PersistedObject nuevo){
    List<Attribute> lista_viejo = viejo.getAttributes();
    List<Attribute> lista_nuevo = nuevo.getAttributes();
    for(int i = 0; i < lista_viejo.size(); i++) {

      if(objeto){
        updateObject()
      }
      lista_viejo.set(i, lista_nuevo.get(i));

    }
  }*/

  private Boolean isObject(Class<?> clazz) {
    return !WRAPPER_TYPE_MAP.containsKey(clazz) && !clazz.equals(String.class);
  }

  private PersistedObject crearPersistedObject(Long sId, Object o) throws IllegalAccessException {
    List<Attribute> attributes = new ArrayList<>();

    Boolean isObjectPersistible = o.getClass().isAnnotationPresent(Persistable.class);
    Field[] fields = o.getClass().getDeclaredFields();
    String className = o.getClass().getCanonicalName();

    for (Field f : fields) {
      if((isObjectPersistible && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible && f.isAnnotationPresent(Persistable.class) )) {
        f.setAccessible(true);
        if(isObject(f.getType())) {
          System.out.println(f.getName() + " es objeto");
          PersistedObject perObj = crearPersistedObject(null, f.get(o));
          EntityManagerHelper.beginTransaction();
          EntityManagerHelper.getEntityManager().persist(perObj);
          EntityManagerHelper.commit();
          Attribute att = new Attribute(f.getName(),f.getType().toString().replace("class ", ""),null, perObj.getId());
          attributes.add(att);
        } else {
          System.out.println(f.getName() + " no es objeto");
          Attribute att = new Attribute(f.getName(), f.getType().toString().replace("class ", ""), f.get(o).toString(),null);
          attributes.add(att);
          }
        }
      }
    return new PersistedObject(sId, className, attributes);
  }

  public <T> T load(long sId, Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";
    return (T) getObjectFromPersistedObjectQuery(hql);
  }

  private Object getObjectFromPersistedObjectQuery(String query) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    PersistedObject object = (PersistedObject) EntityManagerHelper.createQuery(query).getSingleResult();

    Class<?> clazz = Class.forName(object.getClassName());
    Object instance = clazz.newInstance();

    List<Attribute> atts = object.getAttributes();

    for (Attribute att : atts) {

      Class<?> attClazz = Class.forName(att.getDataType());
      Object attInstance = null;

      if (isObject(attClazz)) { //Object Attribute
        String hql = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'";
        attInstance = getObjectFromPersistedObjectQuery(hql);

      } else { //Primitive Attribute
        Constructor<?> attConstructor = attClazz.getConstructor(String.class);
        attInstance = attConstructor.newInstance(att.getValue());
      }

      //Setter
      String attName = att.getName();
      String attNameCapitalized = attName.substring(0, 1).toUpperCase() + attName.substring(1);
      String setterName = "set" + attNameCapitalized;
      Method setter = clazz.getDeclaredMethod(setterName, attClazz);
      setter.invoke(instance, attInstance);
    }
    return instance;
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
