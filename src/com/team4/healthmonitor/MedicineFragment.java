package com.team4.healthmonitor;


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
	private String username;
	private String password;
	
	
	
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
		
		
		Intent i = getActivity().getIntent();
		username = i.getStringExtra(MainActivity.USERNAME);
		password = i.getStringExtra(MainActivity.PASSWORD);
		
		DatabaseHandler db = new DatabaseHandler(getActivity());
		User u = db.login(username, password);
		
		String f = u.getFirstName();
		String l = u.getLastName();
		int a = u.getAge();
		char g = u.getGender();
		
		
		
		//Toast.makeText(getActivity(), username +" "+password, Toast.LENGTH_SHORT).show();
		//Toast.makeText(getActivity(), myContext+"", Toast.LENGTH_SHORT).show();
		
   
		
		
	    TableLayout ll = (TableLayout) rootView.findViewById(R.id.tableLayout);


	    for (int j = 0; j < 4; j++) 
	    {

	        TableRow row= new TableRow(getActivity());
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        TextView aa = new TextView(getActivity());
	        TextView b = new TextView(getActivity());
	        TextView c = new TextView(getActivity());
	        TextView d = new TextView(getActivity());
	        aa.setText(""+f);
	        b.setText(""+l);
	        c.setText(""+a);
	        d.setText(""+g);
	        row.addView(aa);
	        row.addView(b);
	        row.addView(c);
	        row.addView(d);
	        ll.addView(row,j);
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
