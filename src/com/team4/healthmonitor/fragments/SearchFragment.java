package com.team4.healthmonitor.fragments;


import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SearchFragment extends Fragment 
{

	private FragmentActivity myContext;
	private String username;
	private String password;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		setHasOptionsMenu(true);
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.search_menu, menu);
	}
}
