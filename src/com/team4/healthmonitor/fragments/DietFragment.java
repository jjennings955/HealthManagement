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
import com.team4.healthmonitor.adapters.DietAdapter;
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
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    updateData();
		  }

		};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("com.team4.healthmonitor.UPDATEDIET"));
		
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
		
	public void updateData()
	{
		DatabaseHandler db = new DatabaseHandler(getActivity());
		adapter.clear();
		adapter.addAll(db.getUserFoods(userId));
		adapter.notifyDataSetChanged();
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
	        	
	        	showDietDialog();
	            return true;
	        case R.id.settings_item:
	        	Toast.makeText(getActivity(), "Search",
	        		      Toast.LENGTH_SHORT).show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    private void showDietDialog()
    {
        FragmentManager fm = myContext.getSupportFragmentManager();
        DietDialog dd = new DietDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        dd.setArguments(args);
        dd.show(fm, "fragment_edit_name");
    }
}
