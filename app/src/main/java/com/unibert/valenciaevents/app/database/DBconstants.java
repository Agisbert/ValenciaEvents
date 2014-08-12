package com.unibert.valenciaevents.app.database;


public final class DBconstants {
	public static final String TEXT = " text";
	public static final String INTEGER = " integer";
	public static final String REAL = " real";
	public static final String NOMBRE_BD = "KNOW";
	public static final String NUMERIC = "numeric";
	public static final int VERSION_BD = 13;
	//1 ->18/12/2013
	//2 ->19/12/2013
	//3 ->21/12/2013
	//4 ->22/12/2013
	
	
	public static class EventoDB {
		public static final String TABLE_NAME = "EVENTO";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_IDAU = "idAu";
		public static final String COLUMN_TIPO = "tipo";
		public static final String COLUMN_NOMBRE = "nombre";
		public static final String COLUMN_FECHA = "fecha";
//		public static final String COLUMN_ANY = "anyo";
//		public static final String COLUMN_MES = "mes";
//		public static final String COLUMN_DIA = "dia";
//		public static final String COLUMN_HOR= "hora";
//		public static final String COLUMN_MIN = "minutos";
		public static final String COLUMN_PRECIO = "precio";
		public static final String COLUMN_URL = "url";
		public static final String COLUMN_CARACTERISTICA = "caracteristica";
		public static final String COLUMN_DESCRIPCION = "descripcion";
		public static final String COLUMN_LUGAR = "lugar";
		public static final String COLUMN_COORDENADAS = "coordenadas";
		public static final String COLUMN_ASISTIR = "asistir";
		public static final String COLUMN_DIRECCION = "direccion";
		public static final String COLUMN_SUBTIPO = "subtipo";
		
		public static final String SQL_CREATE = 
				"CREATE TABLE " + TABLE_NAME +
				"("+ COLUMN_ID + INTEGER + ", " +
				COLUMN_IDAU + INTEGER + ", " +
				COLUMN_TIPO + TEXT + ", " +
				COLUMN_NOMBRE + TEXT + ", " +
//				COLUMN_ANY + INTEGER + ", " +
//				COLUMN_MES + INTEGER + ", " +
//				COLUMN_DIA + INTEGER + ", " +
//				COLUMN_HOR + INTEGER + ", " +
//				COLUMN_MIN + INTEGER + ", " +
				COLUMN_FECHA + REAL + ", " +
				COLUMN_PRECIO + REAL + ", " +
				COLUMN_URL + TEXT + ", " +
				COLUMN_CARACTERISTICA + TEXT + ", " +
				COLUMN_DESCRIPCION + TEXT + ", " +
				COLUMN_LUGAR + TEXT + ", " +
				COLUMN_COORDENADAS + TEXT + ", " +
				COLUMN_DIRECCION + TEXT + ", " +
				COLUMN_SUBTIPO + TEXT + ", " +
				COLUMN_ASISTIR + INTEGER + ")";
		
		public static final String SQL_DROP=
				"DROP TABLE IF EXISTS "+ TABLE_NAME ;
				
	}
	
	
}
