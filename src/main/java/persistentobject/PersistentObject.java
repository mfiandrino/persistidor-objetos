package persistentobject;

import db.EntityManagerHelper;
import entities.*;

import javax.persistence.NoResultException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  private Boolean isFieldPersistable(Field f, Object o) {
    return (isObjectPersistible(o) && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible(o) && f.isAnnotationPresent(Persistable.class));
  }

  private Class<?> getComponentType(List<?> list) {
    return list.stream().findFirst().map(f -> f.getClass()).orElse(null);
  }

  private PersistedObject crearPersistedObject(Long sId, Object o) throws IllegalAccessException {
    List<Attribute> attributes = new ArrayList<>();

    Field[] fields = o.getClass().getDeclaredFields();
    String className = o.getClass().getCanonicalName();

    for (Field f : fields) {
      if(isFieldPersistable(f,o)) {
        f.setAccessible(true);
        Attribute att;

        //if(f.getType().equals(ArrayList.class)) {
        if(List.class.isAssignableFrom(f.getType())) {
          att = saveCollectionField(f,o);
        }
        else if(isCustomObject(f.getType())) { //Si no es de un tipo primitivo ni de sus wrappers
          att = saveCustomObjectField(f,o);
        }
        else { //Si es un tipo primitivo o alguno de sus wrappers
          att = savePrimitiveField(f,o);
        }
        attributes.add(att); //Agrego el atributo armado
      }
    }
    return new PersistedObject(sId, className, attributes);
  }

  private List<CollectionElement> convertToCollectionElementsList(List<?> list, Field f, Object o) {
    if(isCustomObject(getComponentType(list))) {
      return list.stream()
          .map(e -> {
            PersistedObject perObj = null; //Creo una instancia de PersistedObject para persistirla luego
            try {
              perObj = crearPersistedObject(null, e);
            } catch (IllegalAccessException ex) {
              throw new RuntimeException(ex);
            }
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.getEntityManager().persist(perObj);
            EntityManagerHelper.commit();

            return new CollectionElement(e.getClass().toString().replace("class ", ""), null, perObj.getId());
          }).collect(Collectors.toList());
    }
    else {
      return list.stream()
          .map(e -> new CollectionElement(e.getClass().toString().replace("class ", ""),
              e.toString(),
              null))
          .collect(Collectors.toList());
    }
  }

  private Attribute saveCollectionField(Field f, Object o) throws IllegalAccessException {
    PersistedObject perObj = crearPersistedObject(null, f.get(o)); //Creo una instancia de PersistedObject para persistirla luego

    perObj.setCollectionElements(convertToCollectionElementsList((List<CollectionElement>) f.get(o), f, o));
    EntityManagerHelper.beginTransaction();
    EntityManagerHelper.getEntityManager().persist(perObj);
    EntityManagerHelper.commit();

    return new Attribute(f.getName(),
        f.getType().toString().replace("class ", ""),
        //f.getGenericType().toString(),
        null,
        perObj.getId());
  }

  private Attribute savePrimitiveField(Field f, Object o) throws IllegalAccessException {
    return new Attribute(f.getName(),
        f.getType().toString().replace("class ", ""),
        f.get(o).toString(),
        null);
  }
  private Attribute saveCustomObjectField(Field f, Object o) throws IllegalAccessException {
    PersistedObject perObj = crearPersistedObject(null, f.get(o)); //Creo una instancia de PersistedObject para persistirla luego
    EntityManagerHelper.beginTransaction();
    EntityManagerHelper.getEntityManager().persist(perObj);
    EntityManagerHelper.commit();

    return new Attribute(f.getName(),
        f.getType().toString().replace("class ", ""),
        null,
        perObj.getId());
  }

  public <T> T load(long sId, Class<T> clazz) {

    String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";

    T object = null;

    try {
      object = (T) getObjectFromPersistedObjectQuery(hql);
    } catch (ClassNotFoundException ex) {
      throw new StructureChangedException();
    } catch (InstantiationException ex) {
      throw new StructureChangedException();
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      throw new StructureChangedException();
    } catch (InvocationTargetException ex) {
      throw new StructureChangedException();
    }

    return object;
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

      switch(att.getDataType()){ //Reviso si es un tipo primitivo
        case "int":
          int intValue=Integer.parseInt(att.getValue());
          Method intSetter = clazz.getDeclaredMethod(setterName, int.class);
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
          Method boolSetter = clazz.getDeclaredMethod(setterName,boolean.class);
          boolSetter.invoke(realObject, boolValue);
          break;
        case "double":
          double doubleValue = Double.parseDouble(att.getValue());
          Method doubleSetter = clazz.getDeclaredMethod(setterName,double.class);
          doubleSetter.invoke(realObject, doubleValue);
          break;

        case "float":
          float floatValue = Float.parseFloat(att.getValue());
          Method floatSetter = clazz.getDeclaredMethod(setterName,float.class);
          floatSetter.invoke(realObject, floatValue);
          break;

        case  "long":
          long longValue = Long.parseLong(att.getValue());
          Method longtSetter = clazz.getDeclaredMethod(setterName,long.class);
          longtSetter.invoke(realObject, longValue);
          break;

        case  "short":
          short shortValue = Short.parseShort(att.getValue());
          Method shortSetter = clazz.getDeclaredMethod(setterName,short.class);
          shortSetter.invoke(realObject, shortValue);
          break;

        default://Sino no es un tipo primitivo...
          Class<?> attClazz = Class.forName(att.getDataType()); //Creo una instancia Class de la clase del atributo para poder instanciarlo
          Object attInstance = null;

          //if(attClazz.equals(ArrayList.class)) {
          if(List.class.isAssignableFrom(attClazz)) {
          //if(attClazz.equals(List.class)) {

            String hql = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'";
            PersistedObject persistedArray = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();

            attInstance = persistedArray.getCollectionElements().stream().map(e -> {

              try {
                if(isCustomObject(Class.forName(e.getDataType()))){
                  String hqElement = "from PersistedObject where id = '" + e.getElementObjectId() + "'"; //Lo levanto de la DB
                  return getObjectFromPersistedObjectQuery(hqElement);
                }
                else {
                  switch (e.getDataType()) { //Reviso si es un tipo primitivo
                    case "int":
                      return Integer.parseInt(e.getValue());
                    case "char":
                      return e.getValue().charAt(0);
                    case "boolean":
                      return Boolean.parseBoolean(e.getValue());
                    case "double":
                      return Double.parseDouble(e.getValue());
                    case "float":
                      return Float.parseFloat(e.getValue());
                    case "long":
                      return Long.parseLong(e.getValue());
                    case "short":
                      return Short.parseShort(e.getValue());
                    default:
                      Constructor<?> ElemConstructor = Class.forName(e.getDataType()).getConstructor(String.class);
                      return ElemConstructor.newInstance(e.getValue());
                  }
                }
              } catch (ClassNotFoundException ex) {
                throw new StructureChangedException();
              } catch (InstantiationException ex) {
                throw new StructureChangedException();
              } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
              } catch (NoSuchMethodException ex) {
                throw new StructureChangedException();
              } catch (InvocationTargetException ex) {
                throw new StructureChangedException();
              }
            }).collect(Collectors.toList());

          }
          else if (isCustomObject(attClazz)) { //Si el atributo del objeto original no es un objecto primitivo
              String hql = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'"; //Lo levanto de la DB
              attInstance = getObjectFromPersistedObjectQuery(hql); //Y lo asigno a la instancia del atributo
          }
          else { //Si el atributo es wrapper
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
      object = load(sId, clazz);
      String hql = "from PersistedObject where ssId = " + sId + " and className = '" + clazz.getName() + "'";
      removeObjectFromQuery(hql);
      /*
      PersistedObject persistedObject = (PersistedObject) EntityManagerHelper.createQuery(hql).getSingleResult();

      persistedObject.getAttributes().forEach(att -> {
        if(att.getAttributeObjectId() != null){
          String hql2 = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'"; //Lo levanto de la DB

          EntityManagerHelper.beginTransaction();
          EntityManagerHelper.getEntityManager().remove((PersistedObject) EntityManagerHelper.createQuery(hql2).getSingleResult());
          EntityManagerHelper.commit();
        }
      });
      EntityManagerHelper.beginTransaction();
      EntityManagerHelper.getEntityManager().remove(persistedObject);
      EntityManagerHelper.commit();
    }*/
    }
    return object;
  }
  private void removeObjectFromQuery (String query) {
    PersistedObject persistedObject = (PersistedObject) EntityManagerHelper.createQuery(query).getSingleResult();
    persistedObject.getAttributes().forEach(att -> {
      if(att.getAttributeObjectId() != null){
        String hql = "from PersistedObject where id = '" + att.getAttributeObjectId() + "'"; //Lo levanto de la DB
        removeObjectFromQuery(hql);
      }
    });

    persistedObject.getCollectionElements().forEach(att -> {
      if(att.getElementObjectId() != null){
        String hql = "from PersistedObject where id = '" + att.getElementObjectId() + "'"; //Lo levanto de la DB
        removeObjectFromQuery(hql);
      }
    });

    EntityManagerHelper.beginTransaction();
    EntityManagerHelper.getEntityManager().remove(persistedObject);
    EntityManagerHelper.commit();

  }
} //No borra recursivamente objetos dentro de objetos
