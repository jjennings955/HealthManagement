package com.team4.healthmonitor;


import java.util.Date;
import java.util.List;

import com.team4.database.Medication;
import com.team4.database.VitalSign;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MedicineFragment extends Fragment 
{

	private FragmentActivity myContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Medicine");
    
		LinearLayout l1 = (LinearLayout)rootView.findViewById(R.id.fragment_med);
		
		TextView[] tv = new TextView[4];
	       
	       LinearLayout l =null;
	       List<Medication> vt = MainActivity.dh.getMedications();       
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
		          }
		
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
	
    private void showMedicineDialog()
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        MedicineDialog md = new MedicineDialog();
        md.show(fm, "fragment_edit_name");
    }
}
