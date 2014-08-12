package com.unibert.valenciaevents.app.vlcculture;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.unibert.valenciaevents.app.adaptadores.ListaAsistAdapter;
import com.unibert.valenciaevents.app.adaptadores.ListaPpalAdapter;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.detalle.DetallePrincipal;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;


public class FragmentListaAsistencia extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */

	public List<Evento> listaEventos;

	private ListView listappal;


	public FragmentListaAsistencia() {
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_lista_ppal, container, false);
		listappal = (ListView) rootView.findViewById(R.id.ListaPpal);

		if(listaEventos==null){
			new LoadAsisted(this.getActivity()).execute(Constantes.REQUEST_LISTA_PPAL);
		}else{
			listappal.setAdapter(new ListaPpalAdapter(this.getActivity(),listaEventos));
		}

		listappal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent detail = new Intent(getActivity().getBaseContext(),DetallePrincipal.class);
				Bundle pack = new Bundle();
				detail.putExtra(Constantes.PASS_ACTIVITY,listaEventos.get(arg2).getId());
				detail.putExtra(Constantes.MAP_CLICK, true);
				getActivity().startActivityForResult(detail, Constantes.REQUEST_LISTA_PPAL);
			}
		});

		return rootView;
	}




	public class LoadAsisted extends AsyncTask<Integer, String, Set<Evento>>{

		private WeakReference<Activity> referencia;
		EventoDAO data;

		public LoadAsisted(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Set<Evento> doInBackground(Integer... params) {
			
			listaEventos = data.getListaAsist();

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Set<Evento> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			listappal.setAdapter(new ListaAsistAdapter(referencia.get().getBaseContext(),listaEventos));
		}

	}
}