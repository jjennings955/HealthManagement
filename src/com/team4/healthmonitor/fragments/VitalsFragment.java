package com.team4.healthmonitor.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team4.database.DatabaseHandler;
import com.team4.database.Helper;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.adapters.VitalAdapter;
import com.team4.healthmonitor.dialogs.SettingsDialog;
import com.team4.healthmonitor.dialogs.VitalDialog;



public class VitalsFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;
	private int userId;
	private int offset = 0;
	private FragmentActivity myContext2;
	private DatabaseHandler db;
	private HashMap<Integer, VitalAdapter> adapters;
	private HashMap<Integer, ListView> listViews;
	private HashMap<Integer, TextView> labels;
	private TextView tip;
	private TextView date;
	private Button backButton;
	private Button forwardButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_vitals, container, false);
		setHasOptionsMenu(true);
		Bundle foo = getArguments();
		
		backButton = (Button)rootView.findViewById(R.id.vital_backButton);
		forwardButton = (Button)rootView.findViewById(R.id.vital_forwardButton);
		userId = foo.getInt(Arguments.USERID);
		date = (TextView)rootView.findViewById(R.id.vitals_date);
		db = new DatabaseHandler(getActivity());
		tip = (TextView)rootView.findViewById(R.id.vitalTip);
		  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("com.team4.healthmonitor.UPDATEVITALS"));
		

		updateDate();
		setHasOptionsMenu(true);
		
		ArrayList<VitalSign> bps = db.getUserVitals(userId, VitalSign.BLOOD_PRESSURE, offset);
		ArrayList<VitalSign> weights = db.getUserVitals(userId, VitalSign.WEIGHT, offset);
		ArrayList<VitalSign> cholesterol = db.getUserVitals(userId, VitalSign.CHOLESTEROL, offset);
		ArrayList<VitalSign> bloodsugar = db.getUserVitals(userId, VitalSign.BLOOD_SUGAR, offset);
		VitalAdapter bpadapter;
		VitalAdapter weightadapter;
		VitalAdapter choladapter;
		VitalAdapter sugaradapter;
		backButton = (Button)rootView.findViewById(R.id.vital_backButton);
		forwardButton = (Button)rootView.findViewById(R.id.vital_forwardButton);
		
       backButton.setOnClickListener(new View.OnClickListener() {
    	   @Override
    		public void onClick(View v) {
    			offset--;
    			updateData(VitalSign.WEIGHT);
    			updateData(VitalSign.CHOLESTEROL);
    			updateData(VitalSign.BLOOD_SUGAR);
    			updateData(VitalSign.BLOOD_PRESSURE);
    			updateDate();
    		}
           });
       forwardButton.setOnClickListener(new View.OnClickListener() {
    	   @Override
    		public void onClick(View v) {
    			offset++;
    			updateData(VitalSign.WEIGHT);
    			updateData(VitalSign.CHOLESTEROL);
    			updateData(VitalSign.BLOOD_SUGAR);
    			updateData(VitalSign.BLOOD_PRESSURE);
    			updateDate();
    		}
           });
		
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
	private void updateDate() {
		date.setText(Helper.getDateWithOffset(offset));
		if (offset == 0)
			forwardButton.setVisibility(View.INVISIBLE);
		else
			forwardButton.setVisibility(View.VISIBLE);
	}
	public int getOffset()
	{
		return offset;
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
				adapter.addAll(db.getUserVitals(userId, type, offset));
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
	        	showSettingsDialog(userId);
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
        args.putInt(Arguments.OFFSET, offset);
        vd.setArguments(args);
        vd.show(fm2, "fragment_edit_name");
    }
    
    public void showSettingsDialog(int id)
    {
        FragmentManager fm = myContext2.getSupportFragmentManager();
        SettingsDialog md = new SettingsDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, id);
        md.setArguments(args);
        md.show(fm, "dialog_settings_medicine");
    }
}
