package com.unibert.valenciaevents.app.adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.util.ArrayList;
import java.util.List;




public class ListaTiposAdapter extends BaseAdapter {
	private static List<TipoSelected> itemTipesList;


	private LayoutInflater l_Inflater;

	public ListaTiposAdapter(Context context, List<TipoSelected> results) {
		if(results!=null){
			itemTipesList = results;
		}else{
			itemTipesList = new ArrayList<TipoSelected>();
		}
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return itemTipesList.size();
	}

	public Object getItem(int position) {
		return itemTipesList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder=new ViewHolder();
			convertView = l_Inflater.inflate(R.layout.item_tipo, null);
			holder.Nombre = (TextView) convertView.findViewById(R.id.nombre);
			holder.Foto = (ImageView) convertView.findViewById(R.id.foto_tipo);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.Nombre.setText(itemTipesList.get(position).tipo.toUpperCase());

		String text = itemTipesList.get(position).tipo.toUpperCase();

		if(text.equals("TEATRO")){
			holder.Foto.setImageResource(Constantes.PIN_TEATRO);
		}else if(text.equals("OTROS")){
			holder.Foto.setImageResource(Constantes.PIN_OTROS);
		}else if(text.equals("MUSICA")){
			holder.Foto.setImageResource(Constantes.PIN_MUSICA);
		}else if(text.equals("CINE")){
			holder.Foto.setImageResource(Constantes.PIN_CINE);
		}else if(text.equals("CONFERENCIA")){
			holder.Foto.setImageResource(Constantes.PIN_CONFERENCIA);
		}else if(text.equals("EXPOSICIONES")){
			holder.Foto.setImageResource(Constantes.PIN_EXPO);
		}
		if(!itemTipesList.get(position).selected){
			holder.Foto.setImageResource(R.drawable.marker_disabled);
		}

		return convertView;
	}



	static class ViewHolder {
		TextView Nombre;
		ImageView Foto;
	}
}

