package com.team4.healthmonitor.fragments;


import com.team4.healthmonitor.MainActivity;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.dialogs.DietDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	public void onAttach(Activity activity)
	{
	    myContext=(FragmentActivity) activity;
	    super.onAttach(activity);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.diet_menu, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.add_item_diet:
	        	showVitalDialog();
	            return true;
	        case R.id.settings_item:
	        	Toast.makeText(getActivity(), "Search",
	        		      Toast.LENGTH_SHORT).show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    private void showVitalDialog()
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        DietDialog dd = new DietDialog();
        dd.show(fm, "fragment_edit_name");
    }
}
