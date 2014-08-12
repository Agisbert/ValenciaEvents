package com.unibert.valenciaevents.app.vlcculture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unibert.valenciaevents.app.adaptadores.TipoSelected;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.ListasSerializer;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.database.DBconstants;
import com.unibert.valenciaevents.app.vlcculture.detalle.DetallePrincipal;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DetalleMapa extends Activity {

	private static List<Evento> event;
	private static List<Marker> markers;
	private static List<TipoSelected> selected;
	private static Evento eventoUnico;
	private static GoogleMap map;
	private static TextView titulo;
	private static Marker mark;
	private static boolean clickable;
	private static HashMap<Long, String> adress;
	boolean changed = false;

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode== Constantes.REQUEST_LISTA_PPAL){
			changed = true;
			Long idAux = (Long) data.getExtras().get(DBconstants.EventoDB.COLUMN_ID);

			if(idAux!=null){
				Boolean asiste = (Boolean) data.getExtras().get(DBconstants.EventoDB.COLUMN_ASISTIR);
				for(Evento evento : event){
					if(evento.getId().equals(idAux)){
						evento.setAsistir(asiste);
						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_mapa);


		map = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.map)).getMap();
		markers = new ArrayList<Marker>();

		Bundle b = this.getIntent().getExtras();

		if(b!=null){
			clickable = b.getBoolean(Constantes.MARKER_CLICK);	
		}

		if(!clickable){
			eventoUnico =  (Evento) b.getSerializable(Constantes.PASS_ACTIVITY);
			cambiaTitulo(eventoUnico);
			mapeaPosicion(eventoUnico);
			map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					Toast.makeText(getBaseContext(),getBaseContext().getResources().getString(R.string.Atrasmapa),Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			ListasSerializer aux = (ListasSerializer) b.getSerializable(Constantes.PASS_LIST_TYPES);
			selected = aux.objeto;
			cambiaTituloBusqueda();
			new AddMarkers(this).execute(selected);
			map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					int pos = markers.indexOf(marker);

					Evento evento = null;
					if(pos!=-1){ 
						evento = event.get(pos);
					}
					if(evento != null){
						Intent detail = new Intent(DetalleMapa.this,DetallePrincipal.class);
						Bundle pack = new Bundle();
						pack.putSerializable(Constantes.PASS_ACTIVITY, evento.getId());
						pack.putSerializable(Constantes.MAP_CLICK, false);
						detail.putExtras(pack);
						startActivityForResult(detail, Constantes.REQUEST_LISTA_PPAL);
					}
				}
			});
		}



	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = getIntent();
		if(changed){
			setResult(Constantes.REQUEST_LISTA_PPAL_BD,returnIntent);
		}else{
			setResult(Constantes.REQUEST_LISTA_PPAL_NO,returnIntent);
		}
		super.onBackPressed();
	}     

	private void cambiaTitulo(Evento evento) {
		titulo = (TextView) findViewById(R.id.titulo_mapa);
		titulo.setText(evento.getNombre());
	}	
	private void cambiaTituloBusqueda() {
		titulo = (TextView) findViewById(R.id.titulo_mapa);
		titulo.setText(R.string.TituloBusqueda);
	}

	//Un solo evento. LLamada desde detalle.
	private void mapeaPosicion(Evento evento){
		map.setMyLocationEnabled(true);
		String[] latLong= evento.getCoordenadas().split("@");
		LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

		map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(lugar).tilt(45).zoom(15).build()));


		try {
			if(evento.getDireccion()==null && !evento.getCoordenadas().equals(Constantes.COORDENADAS_NULA)){
				Geocoder geocoder;
				List<Address> addresses;
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				evento.setDireccion(address + " - " + city);
			}



		}	 catch (IOException e) {
			e.printStackTrace();
		}			

		mark = map.addMarker(new MarkerOptions()
		.title(evento.getLugar())
		.snippet(evento.getDireccion())
		.position(lugar));

		mark.setIcon(BitmapDescriptorFactory.fromResource(Utiles.getDrawfromEvent(evento)));

		markers.add(mark);
	}

	private void mapeaPosicion(List<Evento> eventos){
		map.setMyLocationEnabled(true);

		String[] latLong= Constantes.COORDENADAS_BASE.split("@");
		LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

		map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(lugar).tilt(45).zoom(12).build()));

		for(Evento evento : eventos){
			mapeaPosicionUnitario(evento);
		}

	}

	private void mapeaPosicionUnitario(Evento evento){

		String[] latLong= evento.getCoordenadas().split("@");
		LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

		if(evento.getDireccion()==null && !evento.getCoordenadas().equals(Constantes.COORDENADAS_NULA)){
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				evento.setDireccion(address + " - " + city);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mark = map.addMarker(new MarkerOptions()
		.title(evento.getNombre())
		.snippet(evento.getDireccion())
		.position(lugar));

		mark.setIcon(BitmapDescriptorFactory.fromResource(Utiles.getDrawfromEvent(evento)));	

		markers.add(mark);
	}


	//Como el culo.
	public static class AddMarkers extends AsyncTask<List<TipoSelected>, Void, List<Evento>>{

		private WeakReference<Activity> referencia;
		EventoDAO data;
		ProgressDialog dialog;

		public AddMarkers(Activity activity){
			this.referencia = new WeakReference<Activity>(activity);
			this.data = new EventoDAO(referencia.get());
		}	
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(referencia.get());
			dialog.setTitle(referencia.get().getApplicationContext().getResources().getString(R.string.Cargando));
			dialog.setMessage(referencia.get().getApplicationContext().getResources().getString(R.string.Espere));
			dialog.setCancelable(true);
			dialog.show();
		}
		@Override
		protected List<Evento> doInBackground(List<TipoSelected>... params) {
			event = data.getEventosHoyTipo(params[0]);
			return event;
		}
		@Override
		protected void onPostExecute(List<Evento> result) {
			((DetalleMapa)referencia.get()).mapeaPosicion(result);
			dialog.dismiss();
		}

		private HashMap<Long, String> getAdresses(List<Evento> eventos) throws IOException{
			HashMap<Long, String> result = new HashMap<Long, String>();
			Geocoder geocoder;
			geocoder = new Geocoder(referencia.get(), Locale.getDefault());
			List<Address> addresses;
			for(Evento evento : eventos){
				String[] latLong= evento.getCoordenadas().split("@");
				LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));
				addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				result.put(evento.getId(),address + " - " + city);
			}
			return result;

		}

	}
}
