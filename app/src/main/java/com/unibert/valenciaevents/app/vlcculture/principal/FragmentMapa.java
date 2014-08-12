package com.unibert.valenciaevents.app.vlcculture.principal;

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
import com.unibert.valenciaevents.app.vlcculture.detalle.DetallePrincipal;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FragmentMapa extends Fragment {


    private List<Evento> listaEventos;
    private View view;
    private Marker mark;
    private List<Marker> listaMarkers;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_mapa_principal, container, false);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) PaginaPrincipal.fragmentManager
                .findFragmentById(R.id.location_map)).getMap();

        listaMarkers = new ArrayList<Marker>();
        new AddMarkers(this).execute();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                int pos = listaMarkers.indexOf(marker);

                Evento evento = null;
                if (pos != -1) {
                    evento = listaEventos.get(pos);
                }
                if (evento != null) {
                    Intent detail = new Intent(getActivity(), DetallePrincipal.class);
                    Bundle pack = new Bundle();
                    detail.putExtra(Constantes.PASS_ACTIVITY,evento.getId());
                    detail.putExtra(Constantes.MAP_CLICK, true);
                    startActivityForResult(detail, Constantes.REQUEST_LISTA_PPAL);
                }
            }
        });

        return view;
    }

    public void mapeaPosicion(List<Evento> eventos) {
        mMap.setMyLocationEnabled(true);

        String[] latLong = Constantes.COORDENADAS_BASE.split("@");
        LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(lugar).tilt(45).zoom(12).build()));

        for (Evento evento : eventos) {
            mapeaPosicionUnitario(evento);
        }

    }

    public void mapeaPosicionUnitario(Evento evento) {

        String[] latLong = evento.getCoordenadas().split("@");
        LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

        if (evento.getDireccion() == null && !evento.getCoordenadas().equals(Constantes.COORDENADAS_NULA)) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                evento.setDireccion(address + " - " + city);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mark = mMap.addMarker(new MarkerOptions()
                .title(evento.getNombre())
                .snippet(evento.getDireccion())
                .position(lugar));

        mark.setIcon(BitmapDescriptorFactory.fromResource(Utiles.getDrawfromEvent(evento)));

        listaMarkers.add(mark);
    }


    public class AddMarkers extends AsyncTask<List<TipoSelected>, Void, List<Evento>> {

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
        protected List<Evento> doInBackground(List<TipoSelected>... params) {
            listaEventos = data.getEventosHoyTipo();
            return listaEventos;
        }

        @Override
        protected void onPostExecute(List<Evento> result) {
            ((FragmentMapa) referencia).mapeaPosicion(result);
            dialog.dismiss();
        }

        private HashMap<Long, String> getAdresses(List<Evento> eventos) throws IOException {
            HashMap<Long, String> result = new HashMap<Long, String>();
            Geocoder geocoder;
            geocoder = new Geocoder(referencia.getActivity(), Locale.getDefault());
            List<Address> addresses;
            for (Evento evento : eventos) {
                String[] latLong = evento.getCoordenadas().split("@");
                LatLng lugar = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));
                addresses = geocoder.getFromLocation(lugar.latitude, lugar.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                result.put(evento.getId(), address + " - " + city);
            }
            return result;

        }

    }


}