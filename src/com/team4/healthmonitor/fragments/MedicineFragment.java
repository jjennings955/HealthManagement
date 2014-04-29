package com.team4.healthmonitor.fragments;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import com.team4.database.DatabaseHandler;
import com.team4.database.Helper;
import com.team4.database.MedSchedule;
import com.team4.database.Medication;
import com.team4.database.User;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.adapters.MedScheduleAdapter;
import com.team4.healthmonitor.dialogs.EditMedicineDialog;
import com.team4.healthmonitor.dialogs.MedicineDialog;
import com.team4.healthmonitor.dialogs.SettingsDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.team4.healthmonitor.Arguments;

public class MedicineFragment extends Fragment
{

	private FragmentActivity myContext;
	private int userId;
	private TextView tip;
	private int day;
	MedScheduleAdapter adapter;
	TextView selectedDay;	
	public MedicineFragment()
	{

	}
	@Override
	public void onResume() {
	  super.onResume();
	  updateData();

	  // Register mMessageReceiver to receive messages.
	  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
	      new IntentFilter("my-event"));
	}

	// handler for received Intents for the "my-event" event 
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    // Extract data included in the Intent
	    String message = intent.getStringExtra("message");
	    Log.d("receiver", "Got message: " + message);
	    updateData();
	  }
	};

	@Override
	public void onPause() {
	  // Unregister since the activity is not visible
	  LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
	  super.onPause();
	} 
	
	public void updateData()
	{
		adapter.clear();
		adapter.addAll(getSchedule());
		adapter.notifyDataSetChanged();
		//checkTip();
	}
	public ArrayList<MedSchedule> getSchedule()
	{
		DatabaseHandler db = new DatabaseHandler(getActivity());
		User currentUser = db.getUser(userId);
		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser, day, Helper.getDate());
		return scheduleEntries;
	}
	private OnClickListener clickHandler = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ArrayList<Integer> ids = new ArrayList<Integer>(Arrays.asList(new Integer[]{ -1, R.id.med_sun, R.id.med_mon, R.id.med_tue, R.id.med_wed, R.id.med_thu, R.id.med_fri, R.id.med_sat }));
			//((TextView)v).setPaintFlags(0);
			selectedDay.setPaintFlags(0);
			day = ids.indexOf(v.getId());
			updateData();
			selectedDay = (TextView)getActivity().findViewById(ids.get(day));
			Log.w("PHMS", "Day = " + day);
			((TextView)v).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);
		Bundle foo = getArguments();
		tip = (TextView) rootView.findViewById(R.id.medicineT);
		userId = foo.getInt(Arguments.USERID);
		setHasOptionsMenu(true);
		DatabaseHandler db = new DatabaseHandler(getActivity());
		User currentUser = db.getUser(userId);
		day = Helper.getDay();
		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser, day, Helper.getDate());
		Log.w("PHMS", "Helper.getDate() returned! " + Helper.getDate());
	    adapter = new MedScheduleAdapter(this, getActivity(), scheduleEntries);
	    ListView view = (ListView)rootView.findViewById(R.id.medicineList);
	    view.setChoiceMode(ListView.CHOICE_MODE_NONE);
	    view.setAdapter(adapter);
	    view.setSelector(android.R.color.transparent);
	    view.setEmptyView(tip);
	    int viewIds[] = { -1, R.id.med_sun, R.id.med_mon, R.id.med_tue, R.id.med_wed, R.id.med_thu, R.id.med_fri, R.id.med_sat };
	    
	    for (int i = 1 ; i < viewIds.length; i++)
	    {
	    	
	    	TextView current = (TextView)rootView.findViewById(viewIds[i]);
	    	
	    	current.setOnClickListener(clickHandler);
	    	if (i == day)
	    	{
	    		selectedDay = current;
	    		selectedDay.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
	    	}
	    }
	    Log.w("PHMS", "day = " + day);
	    updateData();
	    //checkTip();
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
		//boolean result = true;
		return adapter == null || adapter.getCount() == 0;
		//return true;
	}
		
		
	
	
	@Override
	public void onAttach(Activity activity)
	{
	    myContext=(FragmentActivity) activity;
	    super.onAttach(activity);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.medicine_menu, menu);
	
	   
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.add_item_medicine:
	        	showMedicineDialog();
	            return true;
	        case R.id.settings_item:
	        	showSettingsDialog(userId);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    public void showEditMedicineDialog(int id)
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        EditMedicineDialog md = new EditMedicineDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, id);
        md.setArguments(args);
        md.show(fm, "dialog_edit_medicine");
    }
    public void showSettingsDialog(int id)
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        SettingsDialog md = new SettingsDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, id);
        md.setArguments(args);
        md.show(fm, "dialog_settings_medicine");
    }
    private void showMedicineDialog()
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        MedicineDialog md = new MedicineDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        md.setArguments(args);
        
        md.show(fm, "dialog_add_medicine");
    }
}
