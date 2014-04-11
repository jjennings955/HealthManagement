package com.team4.healthmonitor;


import com.team4.healthmonitor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class VitalsFragment extends Fragment 
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
		setHasOptionsMenu(true);
		
		//((MainAppActivity) getActivity()).setActionBarTitle("Vitals");
		
		return rootView;
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.vitals_menu, menu);
	}
}
