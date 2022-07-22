package test;

import static org.junit.jupiter.api.Assertions.*;

import objects.ClaseFantasma;
import objects.Direccion;
import objects.Persona1;
import objects.Persona4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistentobject.PersistentObject;

import java.lang.reflect.InvocationTargetException;

public class PersistentObjectTest {
  private PersistentObject persistentObject;

  @BeforeEach
  public void initialize() {
    persistentObject = new PersistentObject();
  }

  @Test
  public void prueba() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    ClaseFantasma c = new ClaseFantasma();
    persistentObject.store(1, c);
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

}
