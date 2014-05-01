package com.team4.healthmonitor.fragments;


import java.util.ArrayList;

import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.MainActivity;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.adapters.ArticleAdapter;
import com.team4.healthmonitor.dialogs.DietDialog;
import com.team4.healthmonitor.dialogs.SettingsDialog;
import com.team4.healthmonitor.adapters.DietAdapter;
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
import android.widget.ListView;
import android.widget.Toast;

public class DietFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private String username;
	private String password;
	private DietAdapter adapter;
	private int userId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_diet, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Diet");
		DatabaseHandler db = new DatabaseHandler(getActivity());
		ListView foodList = (ListView)rootView.findViewById(R.id.food_list);
		Bundle arguments = this.getArguments();
		userId = arguments.getInt(Arguments.USERID);
		ArrayList<com.team4.database.FoodJournal> foods = db.getUserFoods(userId);
		adapter = new DietAdapter(this, getActivity(), R.layout.article_item, foods);
		foodList.setAdapter(adapter);
		foodList.setEmptyView(rootView.findViewById(R.id.dietTip));
		
		
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
	        	showSettingsDialog(userId);
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
    
    public void showSettingsDialog(int id)
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        SettingsDialog md = new SettingsDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, id);
        md.setArguments(args);
        md.show(fm, "dialog_settings_medicine");
    }
}
