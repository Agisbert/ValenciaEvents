package com.unibert.valenciaevents.app.vlcculture.detalle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unibert.valenciaevents.app.adaptadores.ListaComentariosAdapter;
import com.unibert.valenciaevents.app.clases.Comentario;
import com.unibert.valenciaevents.app.clases.Response;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;


public class FragmentDetalleComents extends SupportMapFragment {


	ImageButton comentar;
	
	private static ListView listaComentarios;

	private static List<Comentario> listaComents;
	private static SharedPreferences settings;
	private static EditText comentario;
	
	int idUser;
	Long idActividad;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView =  inflater.inflate(R.layout.fragment_coments, container, false);

		comentar = (ImageButton) rootView.findViewById(R.id.send_butt);
		listaComentarios = (ListView) rootView.findViewById(R.id.listComents);
		comentario = (EditText) rootView.findViewById(R.id.comentario_text);		
		settings = getActivity().getSharedPreferences("settings", 0);

		idActividad = this.getActivity().getIntent().getLongExtra(Constantes.PASS_ACTIVITY, 0);
//		new Comentarios(getActivity()).execute(0);
		
		comentar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				idUser = settings.getInt(Constantes.ID, 0);
				if(idUser==0){
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.need_user), Toast.LENGTH_SHORT).show();;
				}else{
					if(comentario.getText().toString().length()>0){
//						new Comentarios(getActivity()).execute(1);
					}
				}
			}
		});

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}


//	public class Comentarios extends AsyncTask < Integer, String, Integer >{
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			if(result == 0){//ha ido bien la descarga
//				listaComentarios.setAdapter(new ListaComentariosAdapter(referencia.get(), listaComents));
//			}else if(result == 1){//descarga jodida
//				Toast.makeText(referencia.get(), referencia.get().getResources().getString(R.string.comentario_down_fail), Toast.LENGTH_SHORT).show();
//			}else if (result == 2){
//				Toast.makeText(getActivity(), referencia.get().getResources().getString(R.string.comentario_ok), Toast.LENGTH_SHORT).show();
//				comentario.setText(referencia.get().getResources().getString(R.string.Comentar));
//				new Comentarios(referencia.get()).execute(0);
//			}else if(result == 3){
//				Toast.makeText(getActivity(), referencia.get().getResources().getString(R.string.comentario_ko), Toast.LENGTH_SHORT).show();
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//		}
//
//		private WeakReference<Activity> referencia;
//
//
//		public Comentarios(Activity fragment){
//			referencia = new WeakReference<Activity>(fragment);
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected Integer doInBackground(Integer... params) {
//			if(params[0]==0){//descarga comentarios
//				Gson gson = new Gson();
//				try{
//					String param0 = "_OPERACION=1";
//					String param1 = "&_ID_A="+ idActividad;
//					URL event = new URL(Constantes.URL_COMENTS + param0 + param1);
//					URLConnection con = event.openConnection();
//					con.setConnectTimeout(Constantes.TIMEOUT_URL);
//					InputStream input = con.getInputStream();
//					Reader reader = new InputStreamReader(input, "UTF-8");
//					Response resp = gson.fromJson(reader, Response.class);
//					if(resp.isOk()){
//						Type listType = new TypeToken<List<Comentario>>() {}.getType();
//						listaComents = (List<Comentario>)gson.fromJson((String) resp.getObject(),listType);
//						return 0;
//					}else{
//						return 1;
//					}
//
//				}catch(Exception e){
//					e.printStackTrace();
//					return 1;
//				}
//			}else if(params[0]==1){
//				Gson gson = new Gson();
//				try{
//					String param0 = "_OPERACION=2";
//					String param1 = "&_ID_A=" + idActividad;
//					String param2 = "&_ID_U=" + idUser;
//					String param3 = "&_COMENT=";
//					URL event = new URL(Constantes.URL_COMENTS + param0 + param1 + param2 + param3 + URLEncoder.encode(comentario.getText().toString(), "UTF-8"));
//					URLConnection con = event.openConnection();
//					con.setConnectTimeout(Constantes.TIMEOUT_URL);
//					InputStream input = con.getInputStream();
//					Reader reader = new InputStreamReader(input, "UTF-8");
//					Response resp = gson.fromJson(reader, Response.class);
//					if(resp.isOk()){
//						return 2;
//					}else{
//						return 3;
//					}
//
//				}catch(Exception e){
//					e.printStackTrace();
//					return 1;
//				}
//			}else{
//				return 1;
//			}
//		}
//	}
}