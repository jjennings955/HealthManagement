package com.team4.healthmonitor;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.team4.database.DatabaseHandler;
import com.team4.database.MedSchedule;
import com.team4.database.Medication;
import com.team4.database.User;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MedicineFragment extends Fragment 
{

	private FragmentActivity myContext;
	private int userId;
	public MedicineFragment()
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);
		Bundle foo = getArguments();
		userId = foo.getInt("userid");
		//Log.w("PHMS", ""+foo.getInt("userid"));
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Medicine");
    
		//LinearLayout l1 = (LinearLayout)rootView.findViewById(R.id.fragment_med);
		
		//TextView[] tv = new TextView[4];
		DatabaseHandler db = new DatabaseHandler(getActivity());
		User currentUser = db.getUser(foo.getInt("userid"));
		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser);
		//ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule()
		//MedSchedule foo = new MedSchedule()
		//scheduleEntries.add(new MedSchedule(1, "Med", "20mg", "2:00", false));
		//scheduleEntries.add(new MedSchedule(2, "Med2", "205mg", "3:00", false));
		//scheduleEntries.add(new MedSchedule(3, "Med3", "120mg", "4:00", false));
		//scheduleEntries.add(new MedSchedule(4, "Med4", "20mg", "2:00", false));
		//scheduleEntries.add(new MedSchedule(5, "Med5", "20mg", "2:00", false));
		//scheduleEntries.add(new MedSchedule(6, "Med6", "20mg", "2:00", false));
	    MedScheduleAdapter adapter = new MedScheduleAdapter(this, getActivity(), scheduleEntries);
	    ListView view = (ListView)rootView;
	    view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    view.setAdapter(adapter);
	    view.setSelector(android.R.color.darker_gray);
	    view.setOnItemClickListener(new OnItemClickListener()
	    {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				ArrayList<View> focusables = arg1.getFocusables(position);
				for (View v : focusables)
				{
					v.setSelected(true);
				}
				//arg1.setSelected(true);
				//view.getFocusables(direction)
			}
	    	
	    });
	       /*LinearLayout l =null;
	       DatabaseHandler db = new DatabaseHandler(getActivity());
	       List<Medication> vt = db.getMedications();       
		     for (Medication cn1 : vt) {
		    	  for(int j1=0; j1<4; j1++){
		    		   l = new LinearLayout(getActivity().getBaseContext());
		    		   l.setLayoutParams(new ViewGroup.LayoutParams(
		    			        ViewGroup.LayoutParams.MATCH_PARENT,
		    			        ViewGroup.LayoutParams.WRAP_CONTENT));
		    		   //l.setBackgroundResource(R.id.myshape_s);

		    		  tv[j1] = new TextView(getActivity());
		    		  tv[j1].setLayoutParams(new ViewGroup.LayoutParams(
		    			        ViewGroup.LayoutParams.WRAP_CONTENT,
		    			        ViewGroup.LayoutParams.WRAP_CONTENT));
		    		  tv[j1].setWidth(150);
		    		  //tv[j1].setText("Dynamic Text!");
		    	  	 //l.addView(tv[j1]);
		    	  }
		    	  Date d = new Date();
		    	  tv[0].setText(""+cn1.getId());
		    	  tv[1].setText(""+cn1.getName());
		    	  tv[2].setText(""+cn1.getPriority());
		    	  tv[3].setText(""+d.getMonth()+"/"+d.getDay()+"/"+d.getYear());
		          
		    	  for(int j1=0; j1<4; j1++){
		    		 // tv[j1] = new TextView(container.getContext());
		    	  	  l.addView(tv[j1]);
		    	  }
		    	  l1.addView(l);
		          }*/
		
		return rootView;
		//return l;
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
	   
		/*	add = (Button) getView().findViewById(R.id.add_item_medicine);
	   
		// add button listener
				add.setOnClickListener(new OnClickListener() {
		 
				  @Override
				  public void onClick(View arg0) {
		 
					// custom dialog
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_medicine);
					dialog.setTitle("Title...");
		 
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.text);
					text.setText("Android custom dialog example!");
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		 
					dialog.show();
				  }
				});*/
	   
	   
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
	        	Toast.makeText(getActivity(), "Search",
	        		      Toast.LENGTH_SHORT).show();
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
        args.putInt("id", id);
        md.setArguments(args);
        md.show(fm, "fragment_edit_name");
    }
    private void showMedicineDialog()
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        MedicineDialog md = new MedicineDialog();
        
        md.show(fm, "fragment_edit_name");
    }
}
