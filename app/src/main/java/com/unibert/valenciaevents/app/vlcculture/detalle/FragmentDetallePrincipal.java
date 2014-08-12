package com.unibert.valenciaevents.app.vlcculture.detalle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.DetalleMapa;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class FragmentDetallePrincipal extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */

	private static TextView titulo;
	private static TextView fecha;
	private static TextView tipo;
	private static TextView desc;
	private static TextView carac;
	private static TextView lugar;
	private static TextView precio;

	private static TextView carac_tit;

	private static ImageButton asistir_butt;
	private static ImageButton share_butt;
	private static ImageButton gplus_butt;
	private static ImageButton face_butt;
	private static ImageButton twitt_butt;

	private static Evento evento;

	public boolean changed = false;

	public FragmentDetallePrincipal() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_detalle_ppal, container, false);

		titulo = (TextView) rootView.findViewById(R.id.titulo_detalle);
		fecha = (TextView) rootView.findViewById(R.id.fecha_detalle);
		tipo = (TextView) rootView.findViewById(R.id.tipo_detalle);
		desc = (TextView) rootView.findViewById(R.id.descripcion_detalle);
		carac_tit = (TextView) rootView.findViewById(R.id.caracteristica);
		carac = (TextView) rootView.findViewById(R.id.caracteristica_detalle);
		lugar = (TextView) rootView.findViewById(R.id.lugar_detalle);
		precio = (TextView) rootView.findViewById(R.id.precio_detalle);
		share_butt = (ImageButton) rootView.findViewById(R.id.shareButton);
		gplus_butt = (ImageButton) rootView.findViewById(R.id.plusButton);
		face_butt = (ImageButton) rootView.findViewById(R.id.faceButton);
		twitt_butt = (ImageButton) rootView.findViewById(R.id.twitterButton);
		asistir_butt = (ImageButton) rootView.findViewById(R.id.addButton);

		Long id = this.getActivity().getIntent().getLongExtra(Constantes.PASS_ACTIVITY, 0);
		new LoadDesc(this.getActivity()).execute(id);

/*
		if(this.getActivity().getIntent().getExtras().getBoolean(Constantes.MAP_CLICK)){
            mapa_butt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(getActivity().getBaseContext(),DetalleMapa.class);
                    Bundle pack = new Bundle();
                    pack.putSerializable(Constantes.PASS_ACTIVITY, evento);
                    detail.putExtras(pack);
                    detail.putExtra(Constantes.MARKER_CLICK , false);
                    startActivity(detail);
                }
            });
        }else{
            mapa_butt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), getActivity().getBaseContext().getResources().getString(R.string.Atrasdetalle), Toast.LENGTH_SHORT).show();
                }
            });
        }
*/



		asistir_butt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changed = true;
				if(evento.isAsistir()){
					evento.setAsistir(false);
				}else{
					evento.setAsistir(true);
				}
				new AsistirTask(getActivity()).execute(evento);
			}
		});

		share_butt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.app_name));
				emailIntent.putExtra(Intent.EXTRA_TEXT, evento.getNombre() + " / " + getActivity().getResources().getString(R.string.Precio) + " " + evento.getPrecio() +"\n"+ evento.getDireccion() +"\n" +evento.getUrl());
				startActivity(emailIntent);
			}
		});
		
		face_butt.setOnClickListener(new clicker("FACEBOOK"));
		gplus_butt.setOnClickListener(new clicker("PLUS"));
		twitt_butt.setOnClickListener(new clicker("TWITTER"));
		
		


		return rootView;
	}





	public static class LoadDesc extends AsyncTask<Long, String, Evento>{

		EventoDAO data;
		private WeakReference<Activity> referencia;

		public LoadDesc(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());
		}	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Evento doInBackground(Long... params) {
			return data.getEventoById(params[0].toString());
		}
		@Override
		protected void onPostExecute(Evento result) {
			titulo.setText(result.getNombre());
			fecha.setText(Utiles.formatFecha(result.getFecha()));
			tipo.setText(result.getTipo().getTipo());
			carac.setText(result.getCaracteristica());
			if(result.getCaracteristica()==null || result.getCaracteristica().trim().isEmpty()){
				carac_tit.setVisibility(View.INVISIBLE);
				carac.setVisibility(View.INVISIBLE);
			}
			lugar.setText(result.getLugar());
			precio.setText(result.getPrecio());
			if(result.isAsistir()){
				asistir_butt.setImageResource(R.drawable.subthis);
			}else{
				asistir_butt.setImageResource(R.drawable.addthis);
			}
			String descripcion = result.getDescripcion();
			if(result.getSubtipo()!=null){
				descripcion += "\n" +  result.getSubtipo();
			}
			desc.setText(descripcion);
			evento = result;
			((DetallePrincipal)referencia.get()).evento=evento;
		}



	}
	public static class AsistirTask extends AsyncTask<Evento, String, String>{

		EventoDAO data;
		private WeakReference<Activity> referencia;

		public AsistirTask(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Evento... params) {
			data.asistirEvento(params[0]);
			return params[0].isAsistir()+"";
		}
		@Override
		protected void onPostExecute(String result) {
			if("true".equals(result)){
				Toast.makeText(referencia.get(), R.string.AsistenciaOK, Toast.LENGTH_SHORT).show();
				((ImageButton)referencia.get().findViewById(R.id.addButton)).setImageResource(R.drawable.subthis);
			}else{
				Toast.makeText(referencia.get(), R.string.AsistenciaKO, Toast.LENGTH_SHORT).show();
				((ImageButton)referencia.get().findViewById(R.id.addButton)).setImageResource(R.drawable.addthis);
			}

		}



	}

	private class clicker implements OnClickListener{

		public clicker(String aPP) {
			super();
			aplicacion = aPP;
		}

		private String aplicacion;

		@Override
		public void onClick(View v) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.app_name));
			shareIntent.putExtra(Intent.EXTRA_TEXT, evento.getNombre() + " / " + getActivity().getResources().getString(R.string.Precio) + " " + evento.getPrecio() +" - "+ evento.getDireccion() +" - "
//			+evento.getUrl()
			);
			PackageManager pm = v.getContext().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
			for (ResolveInfo app : activityList) {
				if (app.activityInfo.name != null && app.activityInfo.name.toUpperCase().contains(aplicacion)) {
					ActivityInfo activity = app.activityInfo;
					ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
					shareIntent.setComponent(name);
					startActivity(shareIntent);
					break;
				}
			}

		}

	}


}