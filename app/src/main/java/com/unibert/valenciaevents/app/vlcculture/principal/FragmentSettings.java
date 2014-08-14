package com.unibert.valenciaevents.app.vlcculture.principal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;
import com.google.gson.Gson;
import com.unibert.valenciaevents.app.clases.Response;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



public class FragmentSettings extends Fragment {


	EditText nombre;
	CheckBox notificaciones;
	RadioButton todos;
	RadioButton free;
	RadioButton family;
	Button guardar;
	RadioGroup grupo;
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;
	private Button mSignInButton;

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_settings, container, false);
/*
		nombre = (EditText) rootView.findViewById(R.id.nombre_edit_sett);
		notificaciones = (CheckBox) rootView.findViewById(R.id.nott_radio);
		grupo = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
		guardar = (Button) rootView.findViewById(R.id.save);
		todos = (RadioButton) rootView.findViewById(R.id.all_radio);
		free = (RadioButton) rootView.findViewById(R.id.free_radio);
		family = (RadioButton) rootView.findViewById(R.id.family_radio);


		settings = getActivity().getSharedPreferences("settings", 0);

		switch (settings.getInt(Constantes.LISTA_CRIT, 0)) {
		case 0:
			todos.setChecked(true);
			break;
		case 1:
			free.setChecked(true);
			break;
		case 2:
			family.setChecked(true);
			break;
		default:
			todos.setChecked(true);
			break;
		}
		editor = settings.edit();*/
/*

		grupo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(todos.getId()==checkedId){
					editor.putInt(Constantes.LISTA_CRIT, 0);// 0 todos, 1 gratis, 2 family.
				}else if(free.getId()==checkedId){
					editor.putInt(Constantes.LISTA_CRIT, 1);
				}else if(family.getId()==checkedId){
					editor.putInt(Constantes.LISTA_CRIT, 2);
				}
				editor.apply();
				((PaginaPrincipal)getActivity()).refreshsettings();
			}
		});

		nombre.setText(settings.getString(Constantes.NOMBRE, ""));
		notificaciones.setChecked(settings.getBoolean(Constantes.NOTIFICACIONES, false));


		if(settings.getInt(Constantes.ID, 0)==0){
			guardar.setText(getActivity().getResources().getString(R.string.alta_usuario));
		}else{
			nombre.setEnabled(false);
			guardar.setText(getActivity().getResources().getString(R.string.borrado));
		}

		guardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(	!nombre.getText().toString().equals("") && //no vacio no vac
						settings.getInt(Constantes.ID, 0)==0){
//					new RegisterUser(getActivity()).execute(0);
				}else if(!nombre.getText().toString().equals("") && settings.getInt(Constantes.ID, 0)!=0){
//					new RegisterUser(getActivity()).execute(1);
				}else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.revisa_datos), Toast.LENGTH_SHORT).show();
				}

			}
		});

		notificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				editor.putBoolean(Constantes.NOTIFICACIONES, isChecked);
				editor.apply();
			}
		});


		mSignInButton = (Button) rootView.findViewById(R.id.plusButtonlog);
*/

		return rootView;
	}





	/*public class RegisterUser extends AsyncTask < Integer, String, Integer >{

		@Override
		protected void onPostExecute(Integer result) {
			if(result==0){
				nombre.setEnabled(false);
				guardar.setText(getActivity().getResources().getString(R.string.borrado));
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.datos_guardados), Toast.LENGTH_SHORT).show();
			}else if(result==1){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_alta_existente), Toast.LENGTH_SHORT).show();
			}else if(result==2){	
				editor.putString(Constantes.NOMBRE, "");
				editor.putInt(Constantes.ID, 0);
				nombre.setText("");
				editor.apply();
				guardar.setText(getActivity().getResources().getString(R.string.alta_usuario));
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.baja_existente), Toast.LENGTH_SHORT).show();
			}else if(result==3){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_baja_existente), Toast.LENGTH_SHORT).show();
			}else if(result==-1){
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_interno), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		private WeakReference<Activity> referencia;
		EventoDAO data;
		Double id = 0D;
		public RegisterUser(Activity fragment){
			this.referencia = new WeakReference<Activity>(fragment);
			this.data = new EventoDAO(referencia.get());

		}

		@Override
		protected Integer doInBackground(Integer... params) {
			Gson gson = new Gson();
			if(params[0]==0){
				try{
					URL event = new URL(Constantes.URL_USERS + URLEncoder.encode(nombre.getText().toString(), "UTF-8"));
					URLConnection con = event.openConnection();
					con.setConnectTimeout(Constantes.TIMEOUT_URL);
					InputStream input = con.getInputStream();
					Reader reader = new InputStreamReader(input, "UTF-8");
					Response resp = gson.fromJson(reader, Response.class);
					if(resp.isOk()){
						id = (Double) resp.getObject();
						editor.putString(Constantes.NOMBRE, nombre.getText().toString());
						editor.putInt(Constantes.ID, id.intValue());
						editor.apply();
						return 0;
					}else{
						return 1;
					}

				}catch(Exception e){
					e.printStackTrace();
					return -1;
				}
			}else if(params[0]==1){
				try{
					URL event = new URL(Constantes.URL_USERS_BAJA + settings.getInt(Constantes.ID, 0));
					URLConnection con = event.openConnection();
					con.setConnectTimeout(Constantes.TIMEOUT_URL);
					InputStream input = con.getInputStream();
					Reader reader = new InputStreamReader(input, "UTF-8");
					Response resp = gson.fromJson(reader, Response.class);
					if(resp.isOk()){
						return 2;
					}else{
						return 3;
					}
				}catch(Exception e){
					e.printStackTrace();
					return -1;
				}
			}
			return -1;


		}
	}*/

}