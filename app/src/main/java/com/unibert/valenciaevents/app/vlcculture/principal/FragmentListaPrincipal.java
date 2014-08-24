package com.unibert.valenciaevents.app.vlcculture.principal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.unibert.valenciaevents.app.adaptadores.ListaPpalAdapter;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.vlcculture.R;
import com.unibert.valenciaevents.app.vlcculture.detalle.DetallePrincipal;

import java.util.List;

public class FragmentListaPrincipal extends Fragment {

	public List<Evento> listaEventos;

	private ListView listappal;
    private TextView lastUpdate;

	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_lista_ppal, container, false);
		listappal = (ListView) rootView.findViewById(R.id.listaPpal);

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

        lastUpdate = (TextView) rootView.findViewById(R.id.lastupdate);

		return rootView;
	}

    public void nuevaLista(List<Evento> listaEventos2){
        listaEventos = listaEventos2;
        listappal.setAdapter(new ListaPpalAdapter(this.getActivity(),listaEventos));
        lastUpdate.setText(getString(R.string.lastUpdate) + this.getActivity().getSharedPreferences(Constantes.LASTUPDATEREF, Context.MODE_PRIVATE).getString(Constantes.LASTUPDATEREF,getString(R.string.noDisponible)));

    }

}