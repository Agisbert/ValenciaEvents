// default package
// Generated 05-sep-2013 13:39:34 by Hibernate Tools 3.4.0.CR1

package com.unibert.valenciaevents.app.clases;

import java.util.Date;

/**
 * Eventos generated by hbm2java
 */


public class Evento implements java.io.Serializable{


	private Long id;
	private Long idAu;

	private Tipo tipo;
	private String nombre;

	private Date fecha;
	private String precio;
	private String url;
	private String caracteristica;
	private String descripcion;
	private String lugar;
	private String subtipo;
	private String coordenadas;
	private boolean asistir;
	private String direccion;

//	@OneToMany(mappedBy = "evento")
//	private Set<Comentario> comentarios = new HashSet<Comentario>(0);


	public boolean isAsistir() {
		return asistir;
	}


	public void setAsistir(boolean asistir) {
		this.asistir = asistir;
	}


	public Evento() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getIdAu() {
		return idAu;
	}


	public void setIdAu(Long idAu) {
		this.idAu = idAu;
	}


	public Tipo getTipo() {
		return tipo;
	}


	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getPrecio() {
		return precio;
	}


	public void setPrecio(String precio) {
		this.precio = precio;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getCaracteristica() {
		return caracteristica;
	}


	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getLugar() {
		return lugar;
	}


	public void setLugar(String lugar) {
		this.lugar = lugar;
	}


	public String getCoordenadas() {
		return coordenadas;
	}


	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getSubtipo() {
		return subtipo;
	}


	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}









}
