// default package
// Generated 05-sep-2013 13:39:34 by Hibernate Tools 3.4.0.CR1

package com.unibert.valenciaevents.app.clases;

import java.util.Date;

/**
 * Comentarios generated by hbm2java
 */

public class Comentario implements java.io.Serializable {


	private Long id;

	private Long evento;

	private Usuario usuario;
	private String texto;

	private Date fecha;

	public Comentario() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEvento() {
		return evento;
	}

	public void setEvento(Long evento) {
		this.evento = evento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


}
