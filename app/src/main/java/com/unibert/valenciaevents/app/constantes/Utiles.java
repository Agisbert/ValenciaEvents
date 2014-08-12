package com.unibert.valenciaevents.app.constantes;

import android.annotation.SuppressLint;

import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utiles {

	public static String formatFecha(Date fecha){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return df.format(fecha);

	}

	public static int getDrawfromEvent(Evento evento){
		String text = evento.getTipo().getTipo().toUpperCase();
		return getDrawfromTipe(text);

	}

	public static int getDrawfromTipe(String text){
		text = text.toUpperCase();
		if(text.equals("TEATRO")){
			return Constantes.PIN_TEATRO;
		}else if(text.equals("OTROS")){
			return Constantes.PIN_OTROS;
		}else if(text.equals("MUSICA")){
			return Constantes.PIN_MUSICA;
		}else if(text.equals("CINE")){
			return Constantes.PIN_CINE;
		}else if(text.equals("CONFERENCIA")){
			return Constantes.PIN_CONFERENCIA;
		}else if(text.equals("EXPOSICIONES")){
			return Constantes.PIN_EXPO;
		}else{
			return R.drawable.question5;
		}

	}	
	
	public static int getBallfromTipe(String text){
		text = text.toUpperCase();
		if(text.equals("TEATRO")){
			return Constantes.BALL_TEATRO;
		}else if(text.equals("OTROS")){
			return Constantes.BALL_OTROS;
		}else if(text.equals("MUSICA")){
			return Constantes.BALL_MUSICA;
		}else if(text.equals("CINE")){
			return Constantes.BALL_CINE;
		}else if(text.equals("CONFERENCIA")){
			return Constantes.BALL_CONFERENCIA;
		}else if(text.equals("EXPOSICIONES")){
			return Constantes.BALL_EXPO;
		}else{
			return R.drawable.question5;
		}

	}	
	
	@SuppressLint("DefaultLocale")
	public static String getColfromTipe(String text){
		text = text.toUpperCase();
		if(text.equals("TEATRO")){
			return Constantes.pastel_blue;
		}else if(text.equals("OTROS")){
			return Constantes.pastel_red;
		}else if(text.equals("MUSICA")){
			return Constantes.pastel_green;
		}else if(text.equals("CINE")){
			return Constantes.pastel_violet;
		}else if(text.equals("CONFERENCIA")){
			return Constantes.pastel_grey;
		}else if(text.equals("EXPOSICIONES")){
			return Constantes.pastel_yellow;
		}else{
			return Constantes.pastel_grey;
		}

	}

	public static String makePlaceholders(int len) {
		if(len == 0) return "";
		StringBuilder sb = new StringBuilder(len * 2 - 1);
		sb.append("?");
		for (int i = 1; i < len; i++) {
			sb.append(",?");
		}
		return sb.toString();

	}
}
