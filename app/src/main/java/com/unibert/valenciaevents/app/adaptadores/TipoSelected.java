package com.unibert.valenciaevents.app.adaptadores;

import java.io.Serializable;

public class TipoSelected implements Serializable{
	public String tipo;
	public boolean selected;
	public TipoSelected(String tipo, boolean selected) {
		super();
		this.tipo = tipo;
		this.selected = selected;
	}

}
