// default package
// Generated 05-sep-2013 13:39:34 by Hibernate Tools 3.4.0.CR1

package com.unibert.valenciaevents.app.clases;

import java.util.Set;

/**
 * Tipos generated by hbm2java
 */

public class Tipo implements java.io.Serializable {

	private String tipo;

	public Tipo() {
	}

	public Tipo(String tipo) {
		this.tipo = tipo;
	}

	public Tipo(String tipo, Set<Evento> evento) {
		this.tipo = tipo;
	}


	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


}
