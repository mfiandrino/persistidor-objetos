package objects;

import entities.NotPersistable;
import entities.Persistable;

@Persistable
public class Direccion {
	private String calle;
	private int numero;
	private String codigoPostal;
	private String localidad;
	private String provincia;
	private String pais;

	public Direccion(){};

	public Direccion(String calle, int numero, String codigoPostal, String localidad, String provincia, String pais) {
		this.calle = calle;
		this.numero = numero;
		this.codigoPostal = codigoPostal;
		this.localidad = localidad;
		this.provincia = provincia;
		this.pais = pais;
	}


	public String getCalle()
	{
		return calle;
	}
	public void setCalle(String calle)
	{
		this.calle=calle;
	}
	public int getNumero()
	{
		return numero;
	}
	public void setNumero(Integer numero)
	{
		this.numero=numero;
	}
	public String getCodigoPostal()
	{
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal)
	{
		this.codigoPostal=codigoPostal;
	}
	public String getLocalidad()
	{
		return localidad;
	}
	public void setLocalidad(String localidad)
	{
		this.localidad=localidad;
	}
	public String getProvincia()
	{
		return provincia;
	}
	public void setProvincia(String provincia)
	{
		this.provincia=provincia;
	}
	public String getPais()
	{
		return pais;
	}
	public void setPais(String pais)
	{
		this.pais=pais;
	}
}
