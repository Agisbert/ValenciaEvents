package com.unibert.valenciaevents.app.vlcculture.detalle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unibert.valenciaevents.app.adaptadores.TipoSelected;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.R;
import com.unibert.valenciaevents.app.vlcculture.principal.PaginaPrincipal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FragmentMapaDetalle extends Fragment {

    public Evento evento;
    private View view;
    private Marker mark;

    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_mapa_detalle, container, false);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) DetallePrincipal.fragmentManager
                .findFragmentById(R.id.location_map_detalle)).getMap();

        Long id = this.getActivity().getIntent().getLongExtra(Constantes.PASS_ACTIVITY, 0);

        new AddMarkers(this).execute(id);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getActivity(),getActivity().getString(R.string.Atrasdetalle),Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void mapeaPosicion(Evento evento){
        mMap.setMyLocationEnabled(true);
        String[] latLong= evento.getCoordenadas().split("@");
        LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(lugar).tilt(45).zoom(15).build()));


        try {
            if(evento.getDireccion()==null && !evento.getCoordenadas().equals(Constantes.COORDENADAS_NULA)){
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                evento.setDireccion(address + " - " + city);
            }



        }	 catch (IOException e) {
            e.printStackTrace();
        }

        mark = mMap.addMarker(new MarkerOptions()
                .title(evento.getLugar())
                .snippet(evento.getDireccion())
                .position(lugar));

        mark.setIcon(BitmapDescriptorFactory.fromResource(Utiles.getDrawfromEvent(evento)));

    }

    public class AddMarkers extends AsyncTask<Long, Void, Evento> {

        EventoDAO data;
        ProgressDialog dialog;
        private Fragment referencia;

        public AddMarkers(Fragment fragment) {
            this.referencia = fragment;
            this.data = new EventoDAO(referencia.getActivity());
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(referencia.getActivity());
            dialog.setTitle(referencia.getActivity().getApplicationContext().getResources().getString(R.string.Cargando));
            dialog.setMessage(referencia.getActivity().getApplicationContext().getResources().getString(R.string.Espere));
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected Evento doInBackground(Long... params) {
            evento = data.getEventoById(params[0].toString());
            return evento;
        }

        @Override
        protected void onPostExecute(Evento result) {
            ((FragmentMapaDetalle) referencia).mapeaPosicion(result);
            dialog.dismiss();
        }

    }


}