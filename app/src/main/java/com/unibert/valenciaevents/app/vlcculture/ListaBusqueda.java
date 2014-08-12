package com.unibert.valenciaevents.app.vlcculture;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.unibert.valenciaevents.app.constantes.Constantes;


public class ListaBusqueda extends FragmentActivity {

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
	
	public boolean changed = false;

	private FragmentListaBusqueda fragmentListaBusqueda;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode== Constantes.REQUEST_LISTA_PPAL){
			fragmentListaBusqueda.new LoadEvents(this).execute(fragmentListaBusqueda.listaTipos);
			changed = true;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagina_principal);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());


		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(1);


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
		if(changed){
			setResult(Constantes.REQUEST_LISTA_PPAL_BD,returnIntent);
		}else{
			setResult(Constantes.REQUEST_LISTA_PPAL_NO,returnIntent);
		}
		super.onBackPressed();
	}     

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		final static int nPaginas = 1;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment=null;
			if(position==0){
				fragment = new FragmentListaBusqueda();
				fragmentListaBusqueda = (FragmentListaBusqueda) fragment;
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
				return getString(R.string.titulo_principal_eventos).toUpperCase();
			}
			return null;
		}
	}




}
