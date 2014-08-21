package com.unibert.valenciaevents.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unibert.valenciaevents.app.adaptadores.TipoSelected;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.clases.Tipo;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.database.DBconstants;
import com.unibert.valenciaevents.app.database.DataBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventoDAO {

	Context context;

	DataBase mDbHelper;

	public EventoDAO(Context context){
		this.context = context;
		this.mDbHelper = new DataBase(context);
	}

	@SuppressWarnings("deprecation")
	public void persist(Evento evento,SQLiteDatabase db){

		ContentValues params = new ContentValues();
		params.put(DBconstants.EventoDB.COLUMN_ID, evento.getId());
		params.put(DBconstants.EventoDB.COLUMN_IDAU, evento.getIdAu());
		params.put(DBconstants.EventoDB.COLUMN_NOMBRE, evento.getNombre());
		params.put(DBconstants.EventoDB.COLUMN_LUGAR, evento.getLugar());
		params.put(DBconstants.EventoDB.COLUMN_TIPO, evento.getTipo().getTipo());
		params.put(DBconstants.EventoDB.COLUMN_PRECIO, evento.getPrecio());
		params.put(DBconstants.EventoDB.COLUMN_CARACTERISTICA, evento.getCaracteristica());

		//		params.put(DBconstants.EventoDB.COLUMN_ANY, evento.getFecha().getYear());
		//		params.put(DBconstants.EventoDB.COLUMN_MES, evento.getFecha().getMonth());
		//		params.put(DBconstants.EventoDB.COLUMN_DIA, evento.getFecha().getDay());
		//		params.put(DBconstants.EventoDB.COLUMN_HOR, evento.getFecha().getHours());
		//		params.put(DBconstants.EventoDB.COLUMN_MIN, evento.getFecha().getMinutes());

		params.put(DBconstants.EventoDB.COLUMN_FECHA, evento.getFecha().getTime());

		params.put(DBconstants.EventoDB.COLUMN_URL, evento.getUrl());
		params.put(DBconstants.EventoDB.COLUMN_DESCRIPCION, evento.getDescripcion());
		params.put(DBconstants.EventoDB.COLUMN_COORDENADAS, evento.getCoordenadas());
		params.put(DBconstants.EventoDB.COLUMN_ASISTIR, (evento.isAsistir())? 1 : 0);
		params.put(DBconstants.EventoDB.COLUMN_DIRECCION, evento.getDireccion());
		params.put(DBconstants.EventoDB.COLUMN_SUBTIPO, evento.getSubtipo());
		//Las coordenadas y la descripcion las usaremos de la llamada a la URL.
		db.insert(DBconstants.EventoDB.TABLE_NAME, null, params);

	}
	
	public void persist(Evento evento){

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		persist(evento, db);
		db.close();
	}

	public void asistirEvento(Evento evento){

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues params = new ContentValues();
		params.put(DBconstants.EventoDB.COLUMN_ASISTIR, (evento.isAsistir())? 1 : 0);
		db.update(DBconstants.EventoDB.TABLE_NAME, params, DBconstants.EventoDB.COLUMN_ID+" = "+evento.getId(), null);
		db.close();

	}

	public void informarDireccion(Evento evento){

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues params = new ContentValues();
		params.put(DBconstants.EventoDB.COLUMN_DIRECCION, evento.getDireccion());
		db.update(DBconstants.EventoDB.TABLE_NAME, params, DBconstants.EventoDB.COLUMN_ID+" = "+evento.getId(), null);
		db.close();

	}
	public void persist(List<Evento> eventos){

		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		for(Evento evento : eventos){
			persist(evento,db);
		}		
		db.close();
	}

	public int getLastID(){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String query = "SELECT MAX("+DBconstants.EventoDB.COLUMN_ID+") FROM " + DBconstants.EventoDB.TABLE_NAME;
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()){
			int ret = cursor.getInt(0);
			db.close();
			return ret;
		}else{
			db.close();
			return 0;
		}


	}

	public List<Evento> getListaPpal(int param){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Date fecha = new Date();
		List<String> parametros = new ArrayList<String>();
		List<Evento> eventos = new ArrayList<Evento>(); 

		parametros.add(fecha.getTime()+"");
		Cursor cursor=null;
		switch (param) {
		case 0:
			cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
					DBconstants.EventoDB.COLUMN_FECHA+" >= ? ORDER BY "+DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));
			break;
		case 1:
			cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
					DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND "+DBconstants.EventoDB.COLUMN_CARACTERISTICA+" LIKE '%ratis%' ORDER BY "+DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));
			break;
		case 2:
			cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
					DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND "+DBconstants.EventoDB.COLUMN_CARACTERISTICA+" LIKE '%miliar%' ORDER BY "+DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));
			break;
		default:
			cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
					DBconstants.EventoDB.COLUMN_FECHA+" >= ? ORDER BY "+DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));
			break;
		}

		while (cursor.moveToNext()) {
			eventos.add(getEventoCursor(cursor, !Constantes.EVENTO_COMPLETO));
		}
		db.close();
		return eventos;
	}

	public List<Evento> getEventosTipo(List<TipoSelected> Tipes){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Date fecha = new Date();
		List<String> parametros = new ArrayList<String>();
		List<Evento> eventos = new ArrayList<Evento>(); 
		int sum = 0;
		parametros.add(fecha.getTime()+"");
		for(TipoSelected aux : Tipes){
			if(aux.selected){
				parametros.add(aux.tipo);
				sum++;
			}
		}

		Cursor cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+ DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND " +
				DBconstants.EventoDB.COLUMN_TIPO + " IN (" + Utiles.makePlaceholders(sum) + " ) ORDER BY "+ DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));

		while (cursor.moveToNext()) {
			eventos.add(getEventoCursor(cursor, !Constantes.EVENTO_COMPLETO));
		}
		db.close();
		return eventos;
	}

	public List<Evento> getEventosHoyTipo(List<TipoSelected> Tipes){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Date fecha = new Date();
		List<String> parametros = new ArrayList<String>();
		List<Evento> eventos = new ArrayList<Evento>(); 
		int sum = 0;
		parametros.add(fecha.getTime()+"");
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.add(Calendar.DATE, 1);
		fecha = cal.getTime();
		parametros.add(fecha.getTime()+"");
		for(TipoSelected aux : Tipes){
			if(aux.selected){
				parametros.add(aux.tipo);
				sum++;
			}
		}

		Cursor cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
				DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND " + DBconstants.EventoDB.COLUMN_FECHA+" <= ? AND "+ 
				DBconstants.EventoDB.COLUMN_TIPO + " IN (" + Utiles.makePlaceholders(sum) + " ) ORDER BY "+ DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));

		while (cursor.moveToNext()) {
			eventos.add(getEventoCursor(cursor, !Constantes.EVENTO_COMPLETO));
		}
		db.close();
		return eventos;
	}

    public List<Evento> getEventosHoyTipo(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Date fecha = new Date();
        List<String> parametros = new ArrayList<String>();
        List<Evento> eventos = new ArrayList<Evento>();
        int sum = 0;
        parametros.add(fecha.getTime()+"");
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, 1);
        fecha = cal.getTime();
        parametros.add(fecha.getTime()+"");

        Cursor cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
                DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND " + DBconstants.EventoDB.COLUMN_FECHA+" <= ? ORDER BY "+ DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));

        while (cursor.moveToNext()) {
            eventos.add(getEventoCursor(cursor, !Constantes.EVENTO_COMPLETO));
        }
        db.close();
        return eventos;
    }

	private Evento getEventoCursor(Cursor cursor, Boolean completo){
		Evento evento = new Evento();
		evento.setId(cursor.getLong(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_ID)));
		evento.setIdAu(cursor.getLong(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_IDAU)));
		evento.setNombre(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_NOMBRE)));
		evento.setLugar(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_LUGAR)));
		evento.setTipo(new Tipo(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_TIPO))));

		evento.setPrecio(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_PRECIO)));
		evento.setLugar(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_LUGAR)));
		evento.setCaracteristica(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_CARACTERISTICA)));

		//		GregorianCalendar aux = new GregorianCalendar(
		//				cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_ANY)),
		//				cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_MES)),
		//				cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_DIA)),
		//				cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_HOR)),
		//				cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_MIN)));
		//		
		//		evento.setFecha(aux.getTime());

		evento.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_FECHA))));
		evento.setUrl(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_URL)));
		evento.setCoordenadas(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_COORDENADAS)));
		evento.setAsistir((cursor.getInt(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_ASISTIR))==1)? true : false);
		evento.setDireccion(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_DIRECCION)));
		evento.setSubtipo(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_SUBTIPO)));

		if(completo){
			evento.setDescripcion(cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_DESCRIPCION)));
		}
		return evento;
	}

	public String getDesc(String string) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT descripcion FROM EVENTO WHERE "+
				DBconstants.EventoDB.COLUMN_ID+" = ?", new String[]{string});
		if(cursor.moveToFirst()){
			String ret = cursor.getString(cursor.getColumnIndex(DBconstants.EventoDB.COLUMN_DESCRIPCION));
			db.close();
			return ret;
		}
		db.close();
		return "";
	}

	public Evento getEventoById(String id){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
				DBconstants.EventoDB.COLUMN_ID+" = ?", new String[]{id});
		if(cursor.moveToFirst()){
			Evento ret = getEventoCursor(cursor, true);
			db.close();
			return ret;
		}
		db.close();
		return null;
	}

	public List<String> getTipos(){

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		List<String> result = new ArrayList<String>();
		String query = "SELECT DISTINCT("+DBconstants.EventoDB.COLUMN_TIPO+") FROM " + DBconstants.EventoDB.TABLE_NAME;
		Cursor cursor = db.rawQuery(query, null);
		while (cursor.moveToNext()) {
			result.add(cursor.getString(0));
		}
		db.close();
		return result;


	}

	public List<Evento> getListaAsist() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Date fecha = new Date();
		List<String> parametros = new ArrayList<String>();
		List<Evento> eventos = new ArrayList<Evento>(); 

		parametros.add(fecha.getTime()+"");
		parametros.add("1");
		Cursor cursor = db.rawQuery("SELECT * FROM EVENTO WHERE "+
				DBconstants.EventoDB.COLUMN_FECHA+" >= ? AND "+ DBconstants.EventoDB.COLUMN_ASISTIR +"= ? ORDER BY "+DBconstants.EventoDB.COLUMN_FECHA+" ASC", parametros.toArray(new String[parametros.size()]));

		while (cursor.moveToNext()) {
			eventos.add(getEventoCursor(cursor, !Constantes.EVENTO_COMPLETO));
		}
		db.close();
		return eventos;
	}


}
