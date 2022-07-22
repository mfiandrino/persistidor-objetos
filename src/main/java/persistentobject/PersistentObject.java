package persistentobject;

import db.EntityManagerHelper;
import entities.*;

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
    WRAPPER_TYPE_MAP.put(Integer.class, Integer.class);
    WRAPPER_TYPE_MAP.put(Byte.class, Byte.class);
    WRAPPER_TYPE_MAP.put(Character.class, Character.class);
    WRAPPER_TYPE_MAP.put(Boolean.class, Boolean.class);
    WRAPPER_TYPE_MAP.put(Double.class, Double.class);
    WRAPPER_TYPE_MAP.put(Float.class, Float.class);
    WRAPPER_TYPE_MAP.put(Long.class, Long.class);
    WRAPPER_TYPE_MAP.put(Short.class, Short.class);
    WRAPPER_TYPE_MAP.put(Void.class, Void.class);
    WRAPPER_TYPE_MAP.put(int.class, Integer.class);
    WRAPPER_TYPE_MAP.put(byte.class, Byte.class);
    WRAPPER_TYPE_MAP.put(char.class, Character.class);
    WRAPPER_TYPE_MAP.put(boolean.class, Boolean.class);
    WRAPPER_TYPE_MAP.put(double.class, Double.class);
    WRAPPER_TYPE_MAP.put(float.class, Float.class);
    WRAPPER_TYPE_MAP.put(long.class, Long.class);
    WRAPPER_TYPE_MAP.put(short.class, Short.class);
    WRAPPER_TYPE_MAP.put(void.class, Void.class);
    WRAPPER_TYPE_MAP.put(String.class, String.class);
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

  private Boolean isCustomObject(Class<?> clazz) {
    return !WRAPPER_TYPE_MAP.containsKey(clazz);
  }

  private Boolean isObjectPersistible(Object o) {
    return o.getClass().isAnnotationPresent(Persistable.class);
  }

  private PersistedObject crearPersistedObject(Long sId, Object o) throws IllegalAccessException {
    List<Attribute> attributes = new ArrayList<>();

    Field[] fields = o.getClass().getDeclaredFields();
    String className = o.getClass().getCanonicalName();

    for (Field f : fields) {
      if((isObjectPersistible(o) && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible(o) && f.isAnnotationPresent(Persistable.class) )) {
        f.setAccessible(true);

       /* if(f.getType().isArray()) {//Si es un array

            Class elementDataType = f.getType().getComponentType();
            Attribute att = new Attribute(f.getName(),
                    f.getType().toString().replace("class ", ""),
                    null,
                    null);

            ArrayList<?> lista = (ArrayList<?>)f.get(o);

            if(isCustomObject(elementDataType)){ //Si es un array de objetos
                for(int i=0;i<lista.size();i++) {

                    Object element = lista.get(i);

                                                                        //No tenemos el id del attribute hasta despuiés de git apersistirlo
                    collectionElement collEl = new collectionElement(i,att.getId(),elementDataType.toString(),lista.get(i).toString(),null);
                }
            }else{
                for(int i=0;i<lista.size();i++) { //Si es un array de primitivos, wrappers o strings
                    collectionElement collEl = new collectionElement(i,att.getId(),elementDataType.toString(),lista.get(i).toString(),null);

                    Habría que agragarlo a una lista de elementos de Attribute y linkearlo con OneToMany
                }
            }


        }else */if(isCustomObject(f.getType())) { //Si no es de un tipo primitivo ni de sus wrappers
            PersistedObject perObj = crearPersistedObject(null, f.get(o)); //Creo una instancia de PersistedObject para persistirla luego
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.getEntityManager().persist(perObj);
            EntityManagerHelper.commit();
            Attribute att = new Attribute(f.getName(),
                f.getType().toString().replace("class ", ""),
            null,
                perObj.getId());

          attributes.add(att);
        }else{ //Si es un tipo primitivo o alguno de sus wrappers

          Attribute att = new Attribute(f.getName(),
              f.getType().toString().replace("class ", ""),
              f.get(o).toString(),
              null);

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
    PersistedObject persistedObject = (PersistedObject) EntityManagerHelper.createQuery(query).getSingleResult(); //Levanto un PersistedObject de la DB

    Class<?> clazz = Class.forName(persistedObject.getClassName()); //Creo una instancia Class de la clase del objecto levantado para poder instanciarlo
    Object realObject = clazz.newInstance(); //Creo una instancia de la clase, consiguiendo un objeto

    List<Attribute> atts = persistedObject.getAttributes(); //Caputuro los atributos del PersistedObject

    for (Attribute att : atts) {
        //Armado del setter del atributo
        String attName = att.getName();
        String attNameCapitalized = attName.substring(0, 1).toUpperCase() + attName.substring(1);
        String setterName = "set" + attNameCapitalized;

        Class[] cArg = new Class[1];//declaro la lista de uno de atributos a pasar a getDeclaredMethod()
        switch(att.getDataType()){ //Reviso si es un tipo primitivo
            case "int":
                int intValue=Integer.parseInt(att.getValue());
                //cArg[0] = int.class;
                Method intSetter = clazz.getDeclaredMethod(setterName,int.class);
                intSetter.invoke(realObject, intValue);
                break;
            case "char":
                char charValue = att.getValue().charAt(0);
                //cArg[0] = char.class;
                Method charSetter = clazz.getDeclaredMethod(setterName,char.class);
                charSetter.invoke(realObject, charValue);
                break;
            case "boolean":
                boolean boolValue = Boolean.parseBoolean(att.getValue());
                cArg[0] = boolean.class;
                Method boolSetter = clazz.getDeclaredMethod(setterName,cArg);
                boolSetter.invoke(realObject, boolValue);
                break;
            case "double":
                double doubleValue = Double.parseDouble(att.getValue());
                cArg[0] = double.class;
                Method doubleSetter = clazz.getDeclaredMethod(setterName,cArg);
                doubleSetter.invoke(realObject, doubleValue);
                break;

            case "float":
                float floatValue = Float.parseFloat(att.getValue());
                //cArg[0] = float.class;
                Method floatSetter = clazz.getDeclaredMethod(setterName,float.class);
                floatSetter.invoke(realObject, floatValue);
                break;

            case  "long":
                long longValue = Long.parseLong(att.getValue());
                cArg[0] = long.class;
                Method longtSetter = clazz.getDeclaredMethod(setterName,cArg);
                longtSetter.invoke(realObject, longValue);
                break;

            case  "short":
                short shortValue = Short.parseShort(att.getValue());
                cArg[0] = short.class;
                Method shortSetter = clazz.getDeclaredMethod(setterName,cArg);
                shortSetter.invoke(realObject, shortValue);
                break;

            default://Sino no es un tipo primitivo...
                Class<?> attClazz = Class.forName(att.getDataType()); //Creo una instancia Class de la clase del atributo para poder instanciarlo
                Object attInstance = null;
                if (isCustomObject(attClazz)) { //Si el atributo del objeto original no es un objecto primitivo
                    String hql = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'"; //Lo levanto de la DB
                    attInstance = getObjectFromPersistedObjectQuery(hql); //Y lo asigno a la instancia del atributo

                } else { //Si el atributo es wrapper
                    Constructor<?> attConstructor = attClazz.getConstructor(String.class); //Obtengo el constructor cuyo parametro recibe un String del atributo
                    attInstance = attConstructor.newInstance(att.getValue()); //Le asigno una instancia
                }

                Method setter = clazz.getDeclaredMethod(setterName, attClazz);
                setter.invoke(realObject, attInstance);
        }
    }
    return realObject;
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
