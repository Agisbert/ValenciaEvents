package com.unibert.valenciaevents.app.vlcculture;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.unibert.valenciaevents.app.adaptadores.ListaTiposAdapter;
import com.unibert.valenciaevents.app.adaptadores.TipoSelected;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.ListasSerializer;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.dao.EventoDAO;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class FragmentBusquedaPrincipal extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	
	
	private static ListView listaCarac;
	
	private static ImageButton mapa_butt;

	private static ImageView foto;
	
	private static List<TipoSelected> tiposSeleccionados;
	
	private static Button BotonLista;
	

	public FragmentBusquedaPrincipal() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_busqueda_ppal, container, false);
		listaCarac = (ListView) rootView.findViewById(R.id.ListaBusqueda);
		listaCarac.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
						foto = (ImageView) arg1.findViewById(R.id.foto_tipo);
						if(((TipoSelected)listaCarac.getAdapter().getItem(arg2)).selected){
							foto.setImageResource(R.drawable.marker_disabled);
							((TipoSelected)listaCarac.getAdapter().getItem(arg2)).selected = false;
						}else{
							foto.setImageResource(Utiles.getDrawfromTipe(((TipoSelected) listaCarac.getAdapter().getItem(arg2)).tipo));
							((TipoSelected)listaCarac.getAdapter().getItem(arg2)).selected = true;
						}
			}
		});
		new LoadTipes(this.getActivity()).execute();
		
		mapa_butt = (ImageButton) rootView.findViewById(R.id.mapa_butt);
		mapa_butt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent detail = new Intent(getActivity().getBaseContext(),DetalleMapa.class);
				Bundle pack = new Bundle();
//				List<Evento> eventos = new EventoDAO(getActivity()).getEventosHoyTipo(tiposSeleccionados);
				ListasSerializer listasSerializer = new ListasSerializer();
				listasSerializer.objeto = tiposSeleccionados;
				pack.putSerializable(Constantes.PASS_LIST_TYPES, listasSerializer);
				detail.putExtras(pack);
				detail.putExtra(Constantes.MARKER_CLICK, true);
				getActivity().startActivityForResult(detail, Constantes.REQUEST_LISTA_PPAL);
			}
		});
		
		BotonLista = (Button) rootView.findViewById(R.id.BotonListaBusqueda);
		BotonLista.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent detail = new Intent(getActivity().getBaseContext(),ListaBusqueda.class);
				Bundle pack = new Bundle();
				ListasSerializer listasSerializer = new ListasSerializer();
				listasSerializer.objeto = tiposSeleccionados;
				pack.putSerializable(Constantes.PASS_LIST_TYPES, listasSerializer);
				detail.putExtras(pack);
				getActivity().startActivityForResult(detail, Constantes.REQUEST_LISTA_PPAL);
			}
		});
		return rootView;
	}
	
	public class LoadTipes extends AsyncTask<Long, String, List<TipoSelected>>{

		EventoDAO data;
		private WeakReference<Activity> referencia;
		
		public LoadTipes(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());
		}	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected List<TipoSelected> doInBackground(Long... params) {
			tiposSeleccionados = new ArrayList<TipoSelected>();
			for(String tipo : data.getTipos()){
				tiposSeleccionados.add(new TipoSelected(tipo, true));
			}
			return tiposSeleccionados;
		}
		@Override
		protected void onPostExecute(List<TipoSelected> result) {
			listaCarac.setAdapter(new ListaTiposAdapter(referencia.get(), result));
		}

		
		
	}


	
}