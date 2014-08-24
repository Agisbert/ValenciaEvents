package com.unibert.valenciaevents.app.adaptadores;

import java.io.Serializable;

public class ModoSelected implements Serializable{
	public String modo;
	public boolean selected;

	public ModoSelected(String tipo, boolean selected) {
		super();
		this.modo = tipo;
		this.selected = selected;
	}

}
