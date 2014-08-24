package com.unibert.valenciaevents.app.vlcculture.principal;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unibert.valenciaevents.app.adaptadores.ListaDrawerAdapter;
import com.unibert.valenciaevents.app.adaptadores.TipoSelected;
import com.unibert.valenciaevents.app.asyncTasks.ThreadDirecciones;
import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.clases.Response;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.constantes.TiposEnum;
import com.unibert.valenciaevents.app.constantes.Utiles;
import com.unibert.valenciaevents.app.dao.EventoDAO;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class PaginaPrincipal extends FragmentActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;

    private ListView mDrawerList;

    private FragmentListaPrincipal fragmentListaPrincipal;

    private FragmentMapa fragmentMapa;

    private FragmentSettings fragmentSettings;

    public static FragmentManager fragmentManager;

    public List<Evento> listaEventosPpal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        fragmentManager = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(4);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        List<TipoSelected> tiposSeleccionados = new ArrayList<TipoSelected>();
        for (TiposEnum tipo : TiposEnum.values()) {
            tiposSeleccionados.add(new TipoSelected(tipo.name(), true));
        }

        mDrawerList.setAdapter(new ListaDrawerAdapter(this,tiposSeleccionados));

        new DownloadEvents().execute(Constantes.REQUEST_LISTA_PPAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final static int nPaginas = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void Refresh(int position) {
            if (position == 1) {
                fragmentListaPrincipal.onResume();
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = null;
            if (position == 1) {//Fragment lista principal
                fragment = new FragmentListaPrincipal();
                fragmentListaPrincipal = (FragmentListaPrincipal) fragment;
            } else if (position == 0) {//Fragment Mapa
                fragment = new FragmentMapa();
                fragmentMapa = (FragmentMapa) fragment;
            } else if (position == 2) { // Fragment Perfil
                fragment = new FragmentSettings();
                fragmentSettings = (FragmentSettings) fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return nPaginas;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.titulo_principal_proximos).toUpperCase();
                case 1:
                    return getString(R.string.titulo_principal_eventos).toUpperCase();
                case 2:
                    return getString(R.string.titulo_principal_perfil).toUpperCase();
            }
            return null;
        }
    }

    public class DownloadEvents extends AsyncTask<Integer, String, Set<Evento>> {

        EventoDAO data;
        ProgressDialog loading;

        public DownloadEvents() {
            this.data = new EventoDAO(PaginaPrincipal.this);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(PaginaPrincipal.this, "", PaginaPrincipal.this.getResources().getString(R.string.Cargando), true, false);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Set<Evento> doInBackground(Integer... params) {
            if (params[0] == Constantes.REQUEST_LISTA_PPAL) {
                Gson gson = new Gson();
                int ultimo = 0;
                try {
                    ultimo = data.getLastID();
                    URL event = new URL(Constantes.URL_DESARROLLO + ultimo);
                    URLConnection con = event.openConnection();
                    con.setConnectTimeout(Constantes.TIMEOUT_URL);
                    InputStream input = con.getInputStream();
                    Reader reader = new InputStreamReader(input, "UTF-8");
                    Response resp = gson.fromJson(reader, Response.class);
                    if (resp.isOk()) {
                        Type listType = new TypeToken<List<Evento>>() {
                        }.getType();
                        listaEventosPpal = (List<Evento>) gson.fromJson((String) resp.getObject(), listType);
                        if (listaEventosPpal.size() > 0) {
                            data.persist(listaEventosPpal);
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(Constantes.LASTUPDATEREF, Context.MODE_PRIVATE).edit();
                        editor.putString(Constantes.LASTUPDATEREF, Utiles.formatFecha(new Date()));
                        editor.commit();
                    } else {
                        listaEventosPpal = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            listaEventosPpal = data.getListaPpal(PaginaPrincipal.this.getSharedPreferences("settings", 0).getInt(Constantes.LISTA_CRIT, 0));

            publishProgress("");
            new Thread(new ThreadDirecciones(PaginaPrincipal.this, data, listaEventosPpal)).start();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            fragmentListaPrincipal.nuevaLista(listaEventosPpal);
//            fragmentSettings.nuevosDatos(listaEventosPpal);

            loading.dismiss();
        }

        @Override
        protected void onPostExecute(Set<Evento> result) {
            super.onPostExecute(result);


        }

    }
}




