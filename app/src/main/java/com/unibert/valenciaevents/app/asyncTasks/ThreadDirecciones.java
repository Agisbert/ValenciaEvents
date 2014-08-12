package com.unibert.valenciaevents.app.asyncTasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.dao.EventoDAO;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ThreadDirecciones implements Runnable{
	
	public ThreadDirecciones(Context context, EventoDAO data,
			List<Evento> eventos) {
		super();
		this.context = context;
		this.data = data;
		this.eventos = eventos;
	}

	Context context;
	EventoDAO data;
	List<Evento> eventos;


	@Override
	public void run() {
		Geocoder geocoder;
		geocoder = new Geocoder(context, Locale.getDefault());

		for(Evento evento : eventos){
			if(evento.getDireccion()==null && !evento.getCoordenadas().equals(Constantes.COORDENADAS_NULA)){
				List<Address> addresses;
				String[] latLong= evento.getCoordenadas().split("@");
				LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));
				try {
					addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
                    if(addresses.size()>0) {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getAddressLine(1);
                        evento.setDireccion(address + " - " + city);
                        data.informarDireccion(evento);
                    }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	


}
