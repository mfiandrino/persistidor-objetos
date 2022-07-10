package objects;

import entities.Persistable;

import java.util.ArrayList;

@Persistable
public class Persona3
{
	private int dni;
	private String nombre;
	private ArrayList<String> telefonos;
	private ArrayList<Direccion> direcciones;
	
	
	
	
	public ArrayList<Direccion> getDirecciones()
	{
		return direcciones;
	}
	public void setDirecciones(ArrayList<Direccion> direcciones)
	{
		this.direcciones=direcciones;
	}
	public ArrayList<String> getTelefonos()
	{
		return telefonos;
	}
	public void setTelefonos(ArrayList<String> telefonos)
	{
		this.telefonos=telefonos;
	}
	public int getDni()
	{
		return dni;
	}
	public void setDni(int dni)
	{
		this.dni=dni;
	}
	public String getNombre()
	{
		return nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	
}
