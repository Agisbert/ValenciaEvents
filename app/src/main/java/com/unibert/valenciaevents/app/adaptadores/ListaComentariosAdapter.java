package com.unibert.valenciaevents.app.adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unibert.valenciaevents.app.clases.Comentario;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.util.ArrayList;
import java.util.List;


public class ListaComentariosAdapter extends BaseAdapter {
	private static List<Comentario> itemComents;


	private LayoutInflater l_Inflater;

	public ListaComentariosAdapter(Context context, List<Comentario> results) {
		if(results!=null){
			itemComents = results;
		}else{
			itemComents = new ArrayList<Comentario>();
		}
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return itemComents.size();
	}

	public Object getItem(int position) {
		return itemComents.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder=new ViewHolder();
			convertView = l_Inflater.inflate(R.layout.item_coment, null);
			holder.Nombre = (TextView) convertView.findViewById(R.id.nombre);
			holder.Texto = (TextView) convertView.findViewById(R.id.texto);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.Nombre.setText(itemComents.get(position).getUsuario().getNombre());
		holder.Texto.setText(itemComents.get(position).getTexto());

		return convertView;
	}



	static class ViewHolder {
		TextView Nombre;
		TextView Texto;
	}
}

