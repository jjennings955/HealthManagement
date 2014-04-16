package com.team4.healthmonitor;


import java.util.ArrayList;

import com.team4.database.DatabaseHandler;
import com.team4.database.User;
import com.team4.healthmonitor.R;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MedicineFragment extends Fragment 
{

	private FragmentActivity myContext;
	private String key = "username";
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Medicine");
		
		
	/*
		Bundle bundle=getArguments(); 
		String username = bundle.getString("username");
		*/
		
		/*	
		Bundle bundle = this.getArguments();
		String username = bundle.getString(key,"username");
	*/	
		
		String username = "";
		Intent i = getActivity().getIntent();
		username = i.getStringExtra(MainActivity.USERNAME);
		
		Toast.makeText(getActivity(), username +"", Toast.LENGTH_SHORT).show();
   
		
		
	    TableLayout ll = (TableLayout) rootView.findViewById(R.id.tableLayout);

	    DatabaseHandler db = new DatabaseHandler(this.getActivity());
	    User dummy = new User();
	    dummy.setId(1);
	    ArrayList<String[]> results = db.getUserMedicationSchedule(dummy);
	    int j = 0;
	    for (String[] resultRow : results) 
	    {

	        TableRow row= new TableRow(getActivity());
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        TextView a = new TextView(getActivity());
	        TextView b = new TextView(getActivity());
	        TextView c = new TextView(getActivity());
	        TextView d = new TextView(getActivity());
	        a.setText(resultRow[0]);
	        b.setText(resultRow[1]);
	        c.setText(resultRow[2]);
	        d.setText(resultRow[3]);
	        row.addView(a);
	        row.addView(b);
	        row.addView(c);
	        row.addView(d);
	        ll.addView(row,j++);
	    }

		
		return rootView;
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
	        	Toast.makeText(getActivity(), "Settings",
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
