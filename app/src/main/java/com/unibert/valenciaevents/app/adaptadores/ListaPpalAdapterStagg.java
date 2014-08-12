package com.unibert.valenciaevents.app.adaptadores;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.util.ArrayList;
import java.util.List;


public class ListaPpalAdapterStagg extends BaseAdapter {
	private List<Evento> itemDetailsrrayList;


	private LayoutInflater l_Inflater;

	public ListaPpalAdapterStagg(Context context, List<Evento> results) {
		if(results!=null){
			itemDetailsrrayList = results;
		}else{
			itemDetailsrrayList = new ArrayList<Evento>();
		}
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		

		if (convertView == null) {
			holder=new ViewHolder();
			convertView = l_Inflater.inflate(R.layout.item_list, null);
			
			holder.Nombre = (TextView) convertView.findViewById(R.id.nombre);
			holder.Fecha = (TextView) convertView.findViewById(R.id.fecha);
//			holder.Lugar = (TextView) convertView.findViewById(R.id.lugar);
			holder.Tipo = (ImageView) convertView.findViewById(R.id.colorid);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		
		holder.Nombre.setText(itemDetailsrrayList.get(position).getNombre());
		holder.Fecha.setText(Utiles.formatFecha(itemDetailsrrayList.get(position).getFecha()));
//		holder.Lugar.setText(itemDetailsrrayList.get(position).getLugar());
//		holder.Tipo.setText(itemDetailsrrayList.get(position).getTipo().getTipo());
//		holder.Tipo.setTextColor(Utiles.getColfromTipe(itemDetailsrrayList.get(position).getTipo().getTipo()));
//		holder.Tipo.setImageResource(Utiles.getBallfromTipe(itemDetailsrrayList.get(position).getTipo().getTipo()));   

		GradientDrawable shapeDrawable = (GradientDrawable)holder.Tipo.getBackground();
		shapeDrawable.setColor(Color.parseColor(Utiles.getColfromTipe(itemDetailsrrayList.get(position).getTipo().getTipo())));

	    
	
		return convertView;
	}



	static class ViewHolder {
		TextView Nombre;
		TextView Fecha;
//		TextView Lugar;
		ImageView Tipo;
	}
	

}

