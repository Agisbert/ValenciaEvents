package com.unibert.valenciaevents.app.vlcculture.principal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.unibert.valenciaevents.app.clases.Evento;
import com.unibert.valenciaevents.app.constantes.Constantes;
import com.unibert.valenciaevents.app.vlcculture.R;

import java.util.List;


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
    ViewPager mViewPager;

    private FragmentListaPrincipal fragmentListaPrincipal;

    private FragmentMapa fragmentMapa;

    private FragmentSettings fragmentSettings;

    public static FragmentManager fragmentManager;

    public List<Evento> listaEventosPpal;

    public void refreshsettings() {
        fragmentListaPrincipal.new DownloadEvents(this).execute(Constantes.REQUEST_LISTA_PPAL_BD);
    }


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
                fragmentMapa=(FragmentMapa) fragment;
            }else if(position==2){ // Fragment Perfil
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

}




