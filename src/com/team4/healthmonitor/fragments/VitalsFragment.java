package com.team4.healthmonitor.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team4.database.DatabaseHandler;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.adapters.VitalAdapter;
import com.team4.healthmonitor.dialogs.VitalDialog;
//import com.team4.healthmonitor.R;


public class VitalsFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;
	private int userId;
	
	private FragmentActivity myContext2;
	private DatabaseHandler db;
	private HashMap<Integer, VitalAdapter> adapters;
	private HashMap<Integer, ListView> listViews;
	private HashMap<Integer, TextView> labels;
	private TextView tip;
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
		tip = (TextView)rootView.findViewById(R.id.vitalTip);
//		getResources().openRawResource(R.raw.nutrition);
		ArrayList<VitalSign> bps = db.getUserVitals(userId, VitalSign.BLOOD_PRESSURE);
		ArrayList<VitalSign> weights = db.getUserVitals(userId, VitalSign.WEIGHT);
		ArrayList<VitalSign> cholesterol = db.getUserVitals(userId, VitalSign.CHOLESTEROL);
		ArrayList<VitalSign> bloodsugar = db.getUserVitals(userId, VitalSign.BLOOD_SUGAR);
		VitalAdapter bpadapter;
		VitalAdapter weightadapter;
		VitalAdapter choladapter;
		VitalAdapter sugaradapter;
		
	    ListView bplist = (ListView)rootView.findViewById(R.id.vitals_bplistview);
	    bpadapter = new VitalAdapter(this, getActivity(), bplist.getId(), bps);
	    
	    ListView weightlist = (ListView)rootView.findViewById(R.id.vitals_weightlistview);
	    weightadapter = new VitalAdapter(this, getActivity(), weightlist.getId(), weights);
	    
	    ListView cholesterolList = (ListView)rootView.findViewById(R.id.vitals_cholesterol_listview);
	    choladapter = new VitalAdapter(this, getActivity(), cholesterolList.getId(), cholesterol);
	    
	    ListView sugarList = (ListView)rootView.findViewById(R.id.vitals_bloodsugar_listview);
	    sugaradapter = new VitalAdapter(this, getActivity(), sugarList.getId(), bloodsugar);
	    listViews = new HashMap<Integer, ListView>();
	    adapters = new HashMap<Integer, VitalAdapter>();
	    listViews.put(VitalSign.BLOOD_PRESSURE, bplist);
	    listViews.put(VitalSign.WEIGHT, weightlist);
	    listViews.put(VitalSign.CHOLESTEROL, cholesterolList);
	    listViews.put(VitalSign.BLOOD_SUGAR, sugarList);
	    
	    adapters.put(VitalSign.BLOOD_PRESSURE, bpadapter);
	    adapters.put(VitalSign.WEIGHT, weightadapter);
	    adapters.put(VitalSign.BLOOD_SUGAR, sugaradapter);
	    adapters.put(VitalSign.CHOLESTEROL, choladapter);
	    labels = new HashMap<Integer, TextView>();
	    labels.put(VitalSign.BLOOD_PRESSURE, (TextView) rootView.findViewById(R.id.vitals_BP_label));
	    labels.put(VitalSign.BLOOD_SUGAR, (TextView) rootView.findViewById(R.id.vitals_bloodsugar_label));
	    labels.put(VitalSign.CHOLESTEROL, (TextView) rootView.findViewById(R.id.vitals_cholesterol_label));
	    labels.put(VitalSign.WEIGHT, (TextView) rootView.findViewById(R.id.vitals_weight_label));
	    
	    //listViews = new ListView[]{ bplist, weightlist, cholesterolList, sugarList };
	    //adapters = new VitalAdapter[]{ bpadapter, weightadapter, choladapter, sugaradapter };
	    listViews.size();
	    
	    for (Entry<Integer, ListView> entry : listViews.entrySet())
	    {
	    	int key = entry.getKey();
	    	ListView val = entry.getValue();
	    	val.setChoiceMode(ListView.CHOICE_MODE_NONE);
	    	val.setAdapter(adapters.get(key));
	    	val.setSelector(android.R.color.transparent);
	    	updateData(key);
	    	
	    }
	    checkTip();
		return rootView;
		
	}
	private void checkTip()
	{
		if (shouldDisplayTip())
			tip.setVisibility(View.VISIBLE);
		else
			tip.setVisibility(View.GONE);
	}
	private boolean shouldDisplayTip()
	{
		boolean result = true;
		for (Entry<Integer, VitalAdapter> entry : adapters.entrySet())
		{
			if (entry.getValue().getCount() > 0)
				return false;
		}
		return true;
	}
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    int type = intent.getIntExtra("type", 0);
		    updateData(type);
		  }
		};

		public void updateData(int type)
		{
			if (adapters.containsKey(type))
			{
				VitalAdapter adapter = adapters.get(type);
				adapter.clear();
				adapter.addAll(db.getUserVitals(userId, type));
				if (adapter.getCount() == 0)
				{
					labels.get(type).setVisibility(View.GONE);
					listViews.get(type).setVisibility(View.GONE);
				}
				else
				{
					labels.get(type).setVisibility(View.VISIBLE);
					listViews.get(type).setVisibility(View.VISIBLE);
				}

				adapter.notifyDataSetChanged();
			}
			checkTip();
			
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
