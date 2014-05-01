package com.team4.healthmonitor.fragments;


import java.util.ArrayList;

import com.team4.database.DatabaseHandler;
import com.team4.database.FoodJournal;
import com.team4.database.Helper;
import com.team4.database.VitalSign;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.MainActivity;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.adapters.ArticleAdapter;
import com.team4.healthmonitor.dialogs.DietDialog;
import com.team4.healthmonitor.dialogs.SearchDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DietFragment extends Fragment 
{
	
	private FragmentActivity myContext;
	private TextView calories;
	private String username;
	private String password;
	private DietAdapter adapter;
	private int userId;
	private TextView date;
	private Button backButton;
	private Button forwardButton;
	private Button today;
	private int offset = 0;
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    int newoffset = intent.getIntExtra("offset", offset);
		    if (newoffset != offset)
		    {
		    	offset = newoffset;
		    }
		    updateData();
		    updateDate();
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
		date = (TextView)rootView.findViewById(R.id.diet_date);
		backButton = (Button)rootView.findViewById(R.id.diet_backButton);
		forwardButton = (Button)rootView.findViewById(R.id.diet_forwardButton);
		userId = arguments.getInt(Arguments.USERID);
		today = (Button)rootView.findViewById(R.id.today_diet);
		calories = (TextView)rootView.findViewById(R.id.diet_calories);
		ArrayList<FoodJournal> foods = db.getUserFoods(userId, offset);
		int sum = 0;
		

		
		adapter = new DietAdapter(this, getActivity(), R.layout.article_item, foods);
		foodList.setAdapter(adapter);
		foodList.setEmptyView(rootView.findViewById(R.id.dietTip));
		  backButton.setOnClickListener(new View.OnClickListener() {
	    	   @Override
	    		public void onClick(View v) {
	    			offset--;
	    			updateData();
	    			updateDate();
	    		}
	           });
	       forwardButton.setOnClickListener(new View.OnClickListener() {
	    	   @Override
	    		public void onClick(View v) {
	    			offset++;
	    			updateData();
	    			updateDate();
	    		}
	           });
	       
	       date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSearchDialog();
			}
		});
	     today.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				offset = 0;
				updateDate();
				updateData();
				
			}
		});
		updateDate();
		updateCalories();
		return rootView;
	}
		
	public void updateData()
	{
		DatabaseHandler db = new DatabaseHandler(getActivity());
		adapter.clear();
		adapter.addAll(db.getUserFoods(userId, offset));
		adapter.notifyDataSetChanged();
		updateCalories();
	}
	private void updateCalories()
	{
		int sum = 0;
		for (int i = 0, cnt = adapter.getCount(); i < cnt; i++)
		{
			sum += adapter.getItem(i).getAmount();
		}
		calories.setText("" + (int)sum);
	}
	private void updateDate() {
		date.setText(Helper.getDateWithOffset(offset));
		if (offset == 0)
		{
			today.setVisibility(View.GONE);
			forwardButton.setVisibility(View.INVISIBLE);
		}
		else
		{
			today.setVisibility(View.VISIBLE);
			forwardButton.setVisibility(View.VISIBLE);
		}
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
        args.putInt(Arguments.OFFSET, offset);
        dd.setArguments(args);
        dd.show(fm, "fragment_edit_name");
    }

	public void showEditDietDialog(int id) {
        FragmentManager fm = myContext.getSupportFragmentManager();
        DietDialog dd = new DietDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        args.putInt(Arguments.OFFSET, offset);
        args.putInt(Arguments.ITEMID, id);
        dd.setArguments(args);
        dd.show(fm, "fragment_edit_name");
		
	}
    private void showSearchDialog()
    {
        FragmentManager fm2 = myContext.getSupportFragmentManager();
        SearchDialog vd = new SearchDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        args.putInt(Arguments.DIALOGTYPE, SearchDialog.DIET_DATE);
        vd.setArguments(args);
        vd.show(fm2, "fragment_edit_name");
    }
}
