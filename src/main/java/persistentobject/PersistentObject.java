package persistentobject;

import db.EntityManagerHelper;
import entities.Attribute;
import entities.NotPersistable;
import entities.Persistable;
import entities.PersistedObject;
import objects.Direccion;

import java.lang.reflect.Constructor;
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

  public <T> T load(long sId, Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

    PersistedObject direRecuperada = (PersistedObject) EntityManagerHelper.createQuery("from PersistedObject where id = '2'").getSingleResult();

    List<Attribute> atts = direRecuperada.getAttributes();

    Class<?> clazz2 = Class.forName(direRecuperada.getClassName());

    Object instance = clazz.newInstance();

    //hay que setear los atributos con los setters de la clase utilizando invokes
    /*for ( cada att){
        instance.getDeclareMethod("set"+att.getName()).invoke();
    }

    System.out.println(instance.getCalle());
    System.out.println(instance.getNumero());

*/

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
