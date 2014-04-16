package com.team4.healthmonitor;


import com.team4.healthmonitor.R;

import android.app.Activity;
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

public class VitalsFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;

	private FragmentActivity myContext2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
		setHasOptionsMenu(true);
		
		//((MainAppActivity) getActivity()).setActionBarTitle("Vitals");
		
		//Toast.makeText(getActivity(), myContext+"", Toast.LENGTH_SHORT).show();
		
		return rootView;
	}
	
	public void onAttach(Activity activity)
	{
	    myContext2=(FragmentActivity) activity;
	    super.onAttach(activity);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.vitals_menu, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.add_item_vitals:
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
        FragmentManager fm2 = myContext2.getSupportFragmentManager();
        VitalDialog vd = new VitalDialog();
        vd.show(fm2, "fragment_edit_name");
    }
}
