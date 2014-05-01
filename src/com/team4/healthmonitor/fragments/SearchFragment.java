package com.team4.healthmonitor.fragments;


import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team4.database.Article;
import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.adapters.SearchResultAdapter;
import com.team4.healthmonitor.dialogs.ArticleSearchDialog;

public class SearchFragment extends Fragment 
{

	private FragmentActivity myContexts;
	private String username;
	private String password;
	private DatabaseHandler db;
	private SearchResultAdapter adapter;
	private ListView listView;
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    String message = intent.getStringExtra("keyword");
		    Log.d("receiver", "Got message: " + message);
		    updateData(message);
		  }
		};

		public void updateData(String message)
		{
			adapter.clear();
			adapter.addAll(db.getArticle_search(message)/*get search results*/);
			adapter.notifyDataSetChanged();
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		
		db = new DatabaseHandler(getActivity());
		ListView rootList = (ListView)rootView.findViewById(R.id.search_list);
		listView = rootList;
		  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("com.team4.healthmonitor.UPDATESEARCH"));
		  ArrayList<Article> arti = new ArrayList<Article>();
		adapter = new SearchResultAdapter(this, getActivity(), R.layout.search_article_item, arti);
		rootList.setAdapter(adapter);
		TextView tipView = (TextView)rootView.findViewById(R.id.searchTip);
		rootList.setEmptyView(tipView);
		
		setHasOptionsMenu(true);
		return rootView;
	}
	
	
	public void onAttach(Activity activity)
	{
	    myContexts=(FragmentActivity) activity;
	    super.onAttach(activity);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.search_item:
	        	
	        	showSearchDialog();
	            return true;
	        case R.id.settings_item:
	        	Toast.makeText(getActivity(), "Search",
	        		      Toast.LENGTH_SHORT).show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    private void showSearchDialog()
    {
        FragmentManager fm = myContexts.getSupportFragmentManager();
        ArticleSearchDialog dd = new ArticleSearchDialog();
        dd.show(fm, "fragment_edit_name");
    }

	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.search_menu, menu);
	}
}







