package com.team4.healthmonitor;


import com.team4.healthmonitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DietFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_diet, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Diet");
		
		Intent i = getActivity().getIntent();
		username = i.getStringExtra(MainActivity.USERNAME);
		password = i.getStringExtra(MainActivity.PASSWORD);
		
		//Toast.makeText(getActivity(), username +" "+password, Toast.LENGTH_SHORT).show();
		//Toast.makeText(getActivity(), myContext+"", Toast.LENGTH_SHORT).show();
		
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.diet_menu, menu);
	}
}
