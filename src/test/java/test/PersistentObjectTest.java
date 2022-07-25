package test;

import static org.junit.jupiter.api.Assertions.*;

import objects.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistentobject.PersistentObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PersistentObjectTest {
  private PersistentObject persistentObject;

  @BeforeEach
  public void initialize() {
    persistentObject = new PersistentObject();
  }

  @Test
  public void unObjetoConAtributosPrimitivosSeGuardaCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Persona1 persona = new Persona1(12345678, "Pedro", 45, 'v');
    persistentObject.store(1, persona);

    Persona1 personaRecuperada = persistentObject.load(1, persona.getClass());

    assertEquals(12345678, personaRecuperada.getDni());
    assertEquals("Pedro", personaRecuperada.getNombre());
    assertEquals(45, personaRecuperada.getAltura());
    assertEquals('v', personaRecuperada.getLetraFavorita());
  }

  @Test
  public void unObjetoConAtributosWrappersDePrimitivosSeGuardaCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion direccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    persistentObject.store(2,direccion);

    Direccion direccionRecuperada = persistentObject.load(2, direccion.getClass());

    assertEquals("Medrano", direccionRecuperada.getCalle());
    assertEquals(22, direccionRecuperada.getNumero());
    assertEquals("C1920", direccionRecuperada.getCodigoPostal());
    assertEquals("CABA", direccionRecuperada.getLocalidad());
    assertEquals("BA", direccionRecuperada.getProvincia());
    assertEquals("Argentina", direccionRecuperada.getPais());
  }

  @Test
  public void unObjetoConOtroObjetoComoAtributoSeGuardaCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion direccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Persona4 persona = new Persona4("Maxi", direccion);
    persistentObject.store(3,persona);

    Persona4 personaRecuperada = persistentObject.load(3, persona.getClass());

    assertEquals("Maxi", personaRecuperada.getNombre());
    assertEquals("Medrano", personaRecuperada.getDireccion().getCalle());
    assertEquals(22, personaRecuperada.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada.getDireccion().getPais());
  }

  @Test
  public void unObjetoExisteEnLaBaseDeDatos() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion direccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Persona4 unaPersona = new Persona4("Maxi", direccion);
    Persona1 otraPersona = new Persona1(12345678, "Pedro", 45, 'v');

    assertFalse(persistentObject.exists(4, unaPersona.getClass()));
    assertFalse(persistentObject.exists(4, otraPersona.getClass()));
    assertFalse(persistentObject.exists(4, direccion.getClass()));

    persistentObject.store(4,unaPersona);
    persistentObject.store(4,otraPersona);
    persistentObject.store(4,direccion);

    assertTrue(persistentObject.exists(4, unaPersona.getClass()));
    assertTrue(persistentObject.exists(4, otraPersona.getClass()));
    assertTrue(persistentObject.exists(4, direccion.getClass()));
  }

  @Test
  public void unObjetoConAtributosPrimitivosSeBorraCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Persona1 persona = new Persona1(12345678, "Pedro", 45, 'v');
    persistentObject.store(5, persona);

    assertTrue(persistentObject.exists(5, persona.getClass()));

    Persona1 personaRecuperada = persistentObject.delete(5, persona.getClass());

    assertFalse(persistentObject.exists(5, persona.getClass()));
    assertEquals(12345678, personaRecuperada.getDni());
    assertEquals("Pedro", personaRecuperada.getNombre());
    assertEquals(45, personaRecuperada.getAltura());
    assertEquals('v', personaRecuperada.getLetraFavorita());
  }

  @Test
  public void unObjetoConAtributosWrappersDePrimitivosSeBorraCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion direccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    persistentObject.store(6,direccion);

    assertTrue(persistentObject.exists(6, direccion.getClass()));

    Direccion direccionRecuperada = persistentObject.delete(6, direccion.getClass());

    assertFalse(persistentObject.exists(6, direccion.getClass()));
    assertEquals("Medrano", direccionRecuperada.getCalle());
    assertEquals(22, direccionRecuperada.getNumero());
    assertEquals("C1920", direccionRecuperada.getCodigoPostal());
    assertEquals("CABA", direccionRecuperada.getLocalidad());
    assertEquals("BA", direccionRecuperada.getProvincia());
    assertEquals("Argentina", direccionRecuperada.getPais());
  }

  @Test
  public void unObjetoConOtroObjetoComoAtributoSeBorraCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion direccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Persona4 persona = new Persona4("Maxi", direccion);
    persistentObject.store(7,persona);

    assertTrue(persistentObject.exists(7, persona.getClass()));

    Persona4 personaRecuperada = persistentObject.delete(7, persona.getClass());

    assertFalse(persistentObject.exists(7, persona.getClass()));
    assertEquals("Maxi", personaRecuperada.getNombre());
    assertEquals("Medrano", personaRecuperada.getDireccion().getCalle());
    assertEquals(22, personaRecuperada.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada.getDireccion().getPais());
  }

  @Test
  public void unObjetoConUnaColeccionComoAtributoSeGuardaCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion unaDireccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Direccion otraDireccion = new Direccion("Medrano2",222,"C19202","CABA2","BA2","Argentina2");
    Direccion otraDireccionMas = new Direccion("Medrano3",223,"C19203","CABA3","BA3","Argentina3");
    ArrayList<Direccion> direcciones = new ArrayList<>();
    direcciones.add(otraDireccion);
    direcciones.add(otraDireccionMas);

    ArrayList<String> telefonos = new ArrayList<>();
    telefonos.add("1234-5678");
    telefonos.add("5678-1234");

    Persona5 persona = new Persona5("Maxi", unaDireccion, direcciones, telefonos);
    persistentObject.store(8,persona);

    Persona5 personaRecuperada = persistentObject.load(8, persona.getClass());

    assertEquals("Maxi", personaRecuperada.getNombre());

    assertEquals("Medrano", personaRecuperada.getDireccion().getCalle());
    assertEquals(22, personaRecuperada.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada.getDireccion().getPais());

    assertEquals("Medrano2", personaRecuperada.getDirecciones().get(0).getCalle());
    assertEquals(222, personaRecuperada.getDirecciones().get(0).getNumero());
    assertEquals("C19202", personaRecuperada.getDirecciones().get(0).getCodigoPostal());
    assertEquals("CABA2", personaRecuperada.getDirecciones().get(0).getLocalidad());
    assertEquals("BA2", personaRecuperada.getDirecciones().get(0).getProvincia());
    assertEquals("Argentina2", personaRecuperada.getDirecciones().get(0).getPais());

    assertEquals("Medrano3", personaRecuperada.getDirecciones().get(1).getCalle());
    assertEquals(223, personaRecuperada.getDirecciones().get(1).getNumero());
    assertEquals("C19203", personaRecuperada.getDirecciones().get(1).getCodigoPostal());
    assertEquals("CABA3", personaRecuperada.getDirecciones().get(1).getLocalidad());
    assertEquals("BA3", personaRecuperada.getDirecciones().get(1).getProvincia());
    assertEquals("Argentina3", personaRecuperada.getDirecciones().get(1).getPais());

    assertEquals("1234-5678", personaRecuperada.getTelefonos().get(0));
    assertEquals("5678-1234", personaRecuperada.getTelefonos().get(1));
  }

  @Test
  public void unObjetoConUnaColeccionComoAtributoSeBorraCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion unaDireccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Direccion otraDireccion = new Direccion("Medrano2",222,"C19202","CABA2","BA2","Argentina2");
    Direccion otraDireccionMas = new Direccion("Medrano3",223,"C19203","CABA3","BA3","Argentina3");
    ArrayList<Direccion> direcciones = new ArrayList<>();
    direcciones.add(otraDireccion);
    direcciones.add(otraDireccionMas);

    ArrayList<String> telefonos = new ArrayList<>();
    telefonos.add("1234-5678");
    telefonos.add("5678-1234");

    Persona5 persona = new Persona5("Maxi", unaDireccion, direcciones, telefonos);
    persistentObject.store(9,persona);

    assertTrue(persistentObject.exists(9, persona.getClass()));

    Persona5 personaRecuperada = persistentObject.delete(9, persona.getClass());

    assertFalse(persistentObject.exists(9, persona.getClass()));

    assertEquals("Maxi", personaRecuperada.getNombre());

    assertEquals("Medrano", personaRecuperada.getDireccion().getCalle());
    assertEquals(22, personaRecuperada.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada.getDireccion().getPais());

    assertEquals("Medrano2", personaRecuperada.getDirecciones().get(0).getCalle());
    assertEquals(222, personaRecuperada.getDirecciones().get(0).getNumero());
    assertEquals("C19202", personaRecuperada.getDirecciones().get(0).getCodigoPostal());
    assertEquals("CABA2", personaRecuperada.getDirecciones().get(0).getLocalidad());
    assertEquals("BA2", personaRecuperada.getDirecciones().get(0).getProvincia());
    assertEquals("Argentina2", personaRecuperada.getDirecciones().get(0).getPais());

    assertEquals("Medrano3", personaRecuperada.getDirecciones().get(1).getCalle());
    assertEquals(223, personaRecuperada.getDirecciones().get(1).getNumero());
    assertEquals("C19203", personaRecuperada.getDirecciones().get(1).getCodigoPostal());
    assertEquals("CABA3", personaRecuperada.getDirecciones().get(1).getLocalidad());
    assertEquals("BA3", personaRecuperada.getDirecciones().get(1).getProvincia());
    assertEquals("Argentina3", personaRecuperada.getDirecciones().get(1).getPais());

    assertEquals("1234-5678", personaRecuperada.getTelefonos().get(0));
    assertEquals("5678-1234", personaRecuperada.getTelefonos().get(1));
  }

  @Test
  public void unObjetoConUnaColeccionDeObjetosComoAtributoSeBorraCorrectamente () throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion unaDireccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Direccion otraDireccion = new Direccion("Medrano2",222,"C19202","CABA2","BA2","Argentina2");
    Direccion otraDireccionMas = new Direccion("Medrano3",223,"C19203","CABA3","BA3","Argentina3");
    /*ArrayList<Direccion> direcciones = new ArrayList<>();
    direcciones.add(otraDireccion);
    direcciones.add(otraDireccionMas);*/

    Persona4 persona1 = new Persona4("Pedro",unaDireccion);
    Persona4 persona2 = new Persona4("Peter",otraDireccion);
    ArrayList<Persona4> habitantes = new ArrayList<>();
    habitantes.add(persona1);
    habitantes.add(persona2);

    /*ArrayList<String> telefonos = new ArrayList<>();
    telefonos.add("1234-5678");
    telefonos.add("5678-1234");*/

    Casa unaCasa = new Casa("La casona", habitantes);

    persistentObject.store(10, unaCasa);

    assertTrue(persistentObject.exists(10, unaCasa.getClass()));

    Casa casaRecuperada = persistentObject.delete(10, unaCasa.getClass());

    assertFalse(persistentObject.exists(10, unaCasa.getClass()));

    assertEquals("La casona", casaRecuperada.getNombre());

    assertEquals("Pedro", casaRecuperada.getHabitantes().get(0).getNombre());

    assertEquals("Peter", casaRecuperada.getHabitantes().get(1).getNombre());

    assertEquals("Medrano", casaRecuperada.getHabitantes().get(0).getDireccion().getCalle());
    assertEquals(22, casaRecuperada.getHabitantes().get(0).getDireccion().getNumero());
    assertEquals("C1920", casaRecuperada.getHabitantes().get(0).getDireccion().getCodigoPostal());
    assertEquals("CABA", casaRecuperada.getHabitantes().get(0).getDireccion().getLocalidad());
    assertEquals("BA", casaRecuperada.getHabitantes().get(0).getDireccion().getProvincia());
    assertEquals("Argentina", casaRecuperada.getHabitantes().get(0).getDireccion().getPais());

    assertEquals("Medrano2", casaRecuperada.getHabitantes().get(1).getDireccion().getCalle());
    assertEquals(222, casaRecuperada.getHabitantes().get(1).getDireccion().getNumero());
    assertEquals("C19202", casaRecuperada.getHabitantes().get(1).getDireccion().getCodigoPostal());
    assertEquals("CABA2", casaRecuperada.getHabitantes().get(1).getDireccion().getLocalidad());
    assertEquals("BA2", casaRecuperada.getHabitantes().get(1).getDireccion().getProvincia());
    assertEquals("Argentina2", casaRecuperada.getHabitantes().get(1).getDireccion().getPais());
  }

  @Test
  public void unObjetoConUnaColeccionDeObjetosConColeccionesComoAtributoSeBorraCorrectamente() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Direccion unaDireccion = new Direccion("Medrano",22,"C1920","CABA","BA","Argentina");
    Direccion otraDireccion = new Direccion("Medrano2",222,"C19202","CABA2","BA2","Argentina2");
    Direccion otraDireccionMas = new Direccion("Medrano3",223,"C19203","CABA3","BA3","Argentina3");
    ArrayList<Direccion> direcciones = new ArrayList<>();
    direcciones.add(otraDireccion);
    direcciones.add(otraDireccionMas);

    ArrayList<String> telefonos = new ArrayList<>();
    telefonos.add("1234-5678");
    telefonos.add("5678-1234");

    Persona5 persona = new Persona5("Maxi", unaDireccion, direcciones, telefonos);
    Persona5 persona2 = new Persona5("Rodri", unaDireccion, direcciones, telefonos);
    ArrayList<Persona5> habitantes = new ArrayList<>();
    habitantes.add(persona);
    habitantes.add(persona2);

    CasaPersonas5 unaCasa = new CasaPersonas5("La casona5", habitantes);

    persistentObject.store(11, unaCasa);

    assertTrue(persistentObject.exists(11, unaCasa.getClass()));

    CasaPersonas5 casaRecuperada = persistentObject.delete(11, unaCasa.getClass());

    assertFalse(persistentObject.exists(11, unaCasa.getClass()));

    assertEquals("La casona5", casaRecuperada.getNombre());

    Persona5 personaRecuperada1 = casaRecuperada.getHabitantes().get(0);
    Persona5 personaRecuperada2 = casaRecuperada.getHabitantes().get(1);

    //PERSONA RECUPERADA 1
    assertEquals("Maxi", personaRecuperada1.getNombre());

    assertEquals("Medrano", personaRecuperada1.getDireccion().getCalle());
    assertEquals(22, personaRecuperada1.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada1.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada1.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada1.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada1.getDireccion().getPais());

    assertEquals("Medrano2", personaRecuperada1.getDirecciones().get(0).getCalle());
    assertEquals(222, personaRecuperada1.getDirecciones().get(0).getNumero());
    assertEquals("C19202", personaRecuperada1.getDirecciones().get(0).getCodigoPostal());
    assertEquals("CABA2", personaRecuperada1.getDirecciones().get(0).getLocalidad());
    assertEquals("BA2", personaRecuperada1.getDirecciones().get(0).getProvincia());
    assertEquals("Argentina2", personaRecuperada1.getDirecciones().get(0).getPais());

    assertEquals("Medrano3", personaRecuperada1.getDirecciones().get(1).getCalle());
    assertEquals(223, personaRecuperada1.getDirecciones().get(1).getNumero());
    assertEquals("C19203", personaRecuperada1.getDirecciones().get(1).getCodigoPostal());
    assertEquals("CABA3", personaRecuperada1.getDirecciones().get(1).getLocalidad());
    assertEquals("BA3", personaRecuperada1.getDirecciones().get(1).getProvincia());
    assertEquals("Argentina3", personaRecuperada1.getDirecciones().get(1).getPais());

    assertEquals("1234-5678", personaRecuperada1.getTelefonos().get(0));
    assertEquals("5678-1234", personaRecuperada1.getTelefonos().get(1));

    //PERSONA RECUPERADA 2
    assertEquals("Rodri", personaRecuperada2.getNombre());

    assertEquals("Medrano", personaRecuperada2.getDireccion().getCalle());
    assertEquals(22, personaRecuperada2.getDireccion().getNumero());
    assertEquals("C1920", personaRecuperada2.getDireccion().getCodigoPostal());
    assertEquals("CABA", personaRecuperada2.getDireccion().getLocalidad());
    assertEquals("BA", personaRecuperada2.getDireccion().getProvincia());
    assertEquals("Argentina", personaRecuperada2.getDireccion().getPais());

    assertEquals("Medrano2", personaRecuperada2.getDirecciones().get(0).getCalle());
    assertEquals(222, personaRecuperada2.getDirecciones().get(0).getNumero());
    assertEquals("C19202", personaRecuperada2.getDirecciones().get(0).getCodigoPostal());
    assertEquals("CABA2", personaRecuperada2.getDirecciones().get(0).getLocalidad());
    assertEquals("BA2", personaRecuperada2.getDirecciones().get(0).getProvincia());
    assertEquals("Argentina2", personaRecuperada2.getDirecciones().get(0).getPais());

    assertEquals("Medrano3", personaRecuperada2.getDirecciones().get(1).getCalle());
    assertEquals(223, personaRecuperada2.getDirecciones().get(1).getNumero());
    assertEquals("C19203", personaRecuperada2.getDirecciones().get(1).getCodigoPostal());
    assertEquals("CABA3", personaRecuperada2.getDirecciones().get(1).getLocalidad());
    assertEquals("BA3", personaRecuperada2.getDirecciones().get(1).getProvincia());
    assertEquals("Argentina3", personaRecuperada2.getDirecciones().get(1).getPais());

    assertEquals("1234-5678", personaRecuperada2.getTelefonos().get(0));
    assertEquals("5678-1234", personaRecuperada2.getTelefonos().get(1));


  }
}
