package objects;

import entities.Persistable;

@Persistable
public class Persona1
{
	private int dni;
	private String nombre;
	private float altura;
	private char letraFavorita;

	public Persona1() {
	}

	public Persona1(int dni, String nombre, float altura, char letraFavorita) {
		this.dni = dni;
		this.nombre = nombre;
		this.altura = altura;
		this.letraFavorita = letraFavorita;
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
	public char getLetraFavorita() {return letraFavorita;}
	public void setLetraFavorita(char letraFavorita) {this.letraFavorita = letraFavorita;}
	public float getAltura() {return altura;}
	public void setAltura(float altura) {this.altura = altura;}
}
