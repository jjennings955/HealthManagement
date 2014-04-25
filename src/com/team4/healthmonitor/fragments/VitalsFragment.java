package com.team4.healthmonitor.fragments;


import java.util.ArrayList;

import com.team4.database.DatabaseHandler;
import com.team4.database.MedSchedule;
import com.team4.database.User;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.adapters.MedScheduleAdapter;
import com.team4.healthmonitor.adapters.VitalAdapter;
import com.team4.healthmonitor.dialogs.VitalDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
 

public class VitalsFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;
	private int userId;
	
	private FragmentActivity myContext2;
	private VitalAdapter adapter;
	private DatabaseHandler db;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
		
		setHasOptionsMenu(true);
		  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("com.team4.healthmonitor.UPDATEVITALS"));
		Bundle foo = getArguments();
		userId = foo.getInt(Arguments.USERID);
		setHasOptionsMenu(true);
		db = new DatabaseHandler(getActivity());
		
		ArrayList<VitalSign> userVitals = db.getUserVitals(userId);
	
	    //adapter = new VitalAdapter(this, getActivity(), userVitals);
	    ListView view = (ListView)rootView.findViewById(R.id.vitals_bplistview);
	    adapter = new VitalAdapter(this, getActivity(), view.getId(), userVitals);
	    view.setChoiceMode(ListView.CHOICE_MODE_NONE);
	    view.setAdapter(adapter);
	    view.setSelector(android.R.color.transparent);
		return rootView;
		
	}
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    String message = intent.getStringExtra("message");
		    Log.d("Vitals", "Got message: " + message);
		    updateData();
		  }
		};

		public void updateData()
		{
			adapter.clear();
			adapter.addAll(db.getUserVitals(userId));
			adapter.notifyDataSetChanged();
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
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        vd.setArguments(args);
        vd.show(fm2, "fragment_edit_name");
    }
}
