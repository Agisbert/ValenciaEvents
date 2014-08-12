package com.unibert.valenciaevents.app.vlcculture.detalle;


import android.content.Intent;
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


public class DetallePrincipal extends FragmentActivity {


    public static FragmentManager fragmentManager;
    public Evento evento;

    SectionsPagerAdapter mSectionsPagerAdapter;
    FragmentDetallePrincipal fragmentDetallePrincipal;
    FragmentMapaDetalle fragmentMapaDetalle;
    FragmentDetalleComents fragmentDetalleComents;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_principal);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        fragmentManager = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_detail);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(4);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        if (fragmentDetallePrincipal.changed) {
            setResult(Constantes.REQUEST_LISTA_PPAL_BD, returnIntent);
        } else {
            setResult(Constantes.REQUEST_LISTA_PPAL_NO, returnIntent);
        }
        super.onBackPressed();
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

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = null;
            if (position == 1) {
                fragment = new FragmentDetallePrincipal();
                fragmentDetallePrincipal = (FragmentDetallePrincipal) fragment;
            } else if (position == 0) {
                fragment = new FragmentMapaDetalle();
                fragmentMapaDetalle = (FragmentMapaDetalle) fragment;
            } else if (position == 2) {
                fragment = new FragmentDetalleComents();
                fragmentDetalleComents = (FragmentDetalleComents) fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return nPaginas;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.titulo_detalle_mapa).toUpperCase();
                case 1:
                    return getString(R.string.titulo_detalle_evento).toUpperCase();
                case 2:
                    return getString(R.string.titulo_detalle_comentarios).toUpperCase();


            }
            return null;
        }
    }


}
