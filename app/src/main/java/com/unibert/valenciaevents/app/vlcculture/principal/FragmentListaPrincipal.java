package com.unibert.valenciaevents.app.vlcculture.principal;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unibert.valenciaevents.app.adaptadores.ListaPpalAdapter;
import com.unibert.valenciaevents.app.asyncTasks.ThreadDirecciones;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.clases.Response;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.detalle.DetallePrincipal;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

public class FragmentListaPrincipal extends Fragment {

	public List<Evento> listaEventos;

	private ListView listappal;

	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_lista_ppal, container, false);
		listappal = (ListView) rootView.findViewById(R.id.ListaPpal);

		if(listaEventos==null){
			new DownloadEvents(this.getActivity()).execute(Constantes.REQUEST_LISTA_PPAL);
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

	public class DownloadEvents extends AsyncTask<Integer, String, Set<Evento>>{

		private WeakReference<Activity> referencia;
		EventoDAO data;
		ProgressDialog loading;

		public DownloadEvents(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading = ProgressDialog.show(referencia.get(),"", referencia.get().getResources().getString(R.string.Cargando), true,false);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Set<Evento> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			if(params[0]==Constantes.REQUEST_LISTA_PPAL){
				Gson gson = new Gson();
				int ultimo=0;
				try{
					ultimo = data.getLastID();
					URL event = new URL(Constantes.URL_DESARROLLO + ultimo);
					URLConnection con = event.openConnection();
					con.setConnectTimeout(Constantes.TIMEOUT_URL);
					InputStream input = con.getInputStream();
					Reader reader = new InputStreamReader(input, "UTF-8");
					Response resp = gson.fromJson(reader, Response.class);
					if(resp.isOk()){
						Type listType = new TypeToken<List<Evento>>() {}.getType();
						listaEventos = (List<Evento>)gson.fromJson((String) resp.getObject(),listType);
						if(listaEventos.size()>0){
							data.persist(listaEventos);
						}
					}else{
						listaEventos = null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			listaEventos = data.getListaPpal(referencia.get().getSharedPreferences("settings", 0).getInt(Constantes.LISTA_CRIT, 0));

			publishProgress("");
			new Thread(new ThreadDirecciones(referencia.get(), data, listaEventos)).start();;

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			listappal.setAdapter(new ListaPpalAdapter(referencia.get().getBaseContext(),listaEventos));
			loading.dismiss();
		}

		@Override
		protected void onPostExecute(Set<Evento> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}
}