package com.example.tppatrones;

import java.lang.reflect.Field;
import java.sql.Types;

public class PersistentObject {
    // Almacena la instancia del objeto o asociada a la clave sId,
    // o actualiza la instancia existente retornando true o false
    // segun actualiza o almacena.
    // El objeto o puede ser null, en tal caso el valor que se
    // almacenara sera null.
    public boolean store(long sId,Object o) throws IllegalAccessException {
        saveObject(o, 1);
        return true;
    };
    // Devuelve la instancia del objeto o asociada a la clave sId.
    public <T> T load(long sId,Class<T> clazz){
        //if(this.exists(sId, clazz){}
        //load objecto persistido. instanciate class.
        //load attributtes.
        //return object.
        T o = null;
        return o;
    };
    // Retorna true o false según exista o un una instancia
// de clazz (aunque sea null) asociada a la clave sId.
    public <T> boolean exists(long sId, Class<T> clazz){
        String className = clazz.getSimpleName();
        String query = "select * from objeto_persistido " +
        "join classes c on c.id op.class_id "+
        "where c.nombre=="+className+" and op.sid == "+sId+"";
        //if query  result not empty return true, else return false
        return true;
    };
    // Retorna (en milisegundos) el tiempo transcurrido
// desde el ultimo acceso registrado para la clave sId,
// sin considerar las llamadas a este metodo ni a exists.

    public long elapsedTime(long sId){
        return 1;
    };
    // retorna y elimina la instancia de clazz vinculada a la
// clave sId, o retorna null si no existe dicha instancia
    /*public <T> T delete(long sId,Class<T> clazz){

    };
    */

    public <T> T delete(long sId, Class<T> clazz) {
        String className = clazz.getSimpleName();
        // select * from objeto_persistido
        // join classes c on c.id op.class_id
        // where c.nombre=='+className+' and op.sid == +sId
        //load  instance in object(if exists we must return it, else we return false)


        //if query finds object. We must delete data. we got OBJECTID.

        // delete from collection_elem where attr_id in (select id from atributo where object_id=+OBJECTID
        // delete from atributo where object_id=+OBJECTID
        // delete from objecto_persistido where object_id == +OBJECTID
        T o = null;
        return o;
    };
    public void saveObject(Object o, long sID) throws IllegalAccessException {
        Boolean isObjectPersistible = o.getClass().isAnnotationPresent(Persistable.class);
        Field[] fields = o.getClass().getDeclaredFields();
        String className = o.getClass().getSimpleName();
        System.out.println(className);
        //
        for(Field f: fields){
            if((isObjectPersistible && !f.isAnnotationPresent(NotPersistable.class)) || (!isObjectPersistible && f.isAnnotationPresent(Persistable.class) )) {
                //Checks if field is object.
                if( !f.getType().isPrimitive() && !f.getType().getSimpleName().equals("String")){
                    f.setAccessible(true);
                    Object val = f.get(o);
                    // if this field contains an object, function call itself.
                    saveObject(val, sID);
                } else if(f.getClass().isArray()){ //what happens if it is a list or any other collection type?
                    //save as array method
                    // saves the field in database, then saves each field of the array in the database.
                    // if array of primitive values or array of objects.

                } else {
                    //is primitive field. Save directly in attribute table.
                    f.setAccessible(true);
                    Object value = f.get(o);
                    String type = f.getGenericType().toString();
                    System.out.println(value);
                    //save value in database using object id
                }

            }
        }
    }
}