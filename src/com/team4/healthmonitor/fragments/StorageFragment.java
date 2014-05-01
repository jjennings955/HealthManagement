package com.team4.healthmonitor.fragments;


import java.util.ArrayList;
import java.util.Map.Entry;

import com.team4.database.Article;
import com.team4.database.DatabaseHandler;
import com.team4.database.User;
import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;
import com.team4.healthmonitor.adapters.ArticleAdapter;
import com.team4.healthmonitor.adapters.VitalAdapter;
import com.team4.healthmonitor.dialogs.EditMedicineDialog;
import com.team4.healthmonitor.dialogs.SettingsDialog;
import com.team4.healthmonitor.dialogs.StorageDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StorageFragment extends Fragment 
{
	private TextView tip;
	private FragmentActivity myContext;
	private String username;
	private String password;
	private int userId;
	private ArticleAdapter adapter;
	private DatabaseHandler db;
	private ListView listView;
	public StorageFragment() {
		// TODO Auto-generated constructor stub
	}
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    String message = intent.getStringExtra("message");
		    Log.d("receiver", "Got message: " + message);
		    updateData();
		  }
		};

		public void updateData()
		{
			adapter.clear();
			adapter.addAll(db.getUserArticle(userId));
			adapter.notifyDataSetChanged();
		}

		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
		ListView rootList = (ListView)rootView.findViewById(R.id.storage_list);
		tip = (TextView)rootView.findViewById(R.id.storageTip);
		listView = rootList;
		  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
			      new IntentFilter("com.team4.healthmonitor.UPDATESTORAGE"));
		db = new DatabaseHandler(getActivity());
		Bundle args = getArguments();
		userId = args.getInt(Arguments.USERID, -1);
		User currentUser = db.getUser(userId);
		ArrayList<Article> articles = db.getUserArticle(userId);
		adapter = new ArticleAdapter(this, getActivity(), R.layout.article_item, articles);
		rootList.setAdapter(adapter);
		rootList.setEmptyView(tip);
		setHasOptionsMenu(true);
	
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
	
    public void showEditArticleDialog(int id)
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        StorageDialog md = new StorageDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        args.putInt(Arguments.ITEMID, id);
        args.putBoolean(Arguments.DIALOGTYPE, StorageDialog.UPDATE);
        md.setArguments(args);
        md.show(fm, "dialog_edit_article");
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.add_item_storage:
	        	showStorageDialog();
	            return true;
	        case R.id.settings_item:
	        	showSettingsDialog(userId);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    private void showStorageDialog()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        StorageDialog storage = new StorageDialog();
        Bundle args = new Bundle();
        args.putInt(Arguments.USERID, userId);
        args.putBoolean(Arguments.DIALOGTYPE, StorageDialog.CREATE);
        storage.setArguments(args);
        
        storage.show(fm, "fragment_add_storage");
    }
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.storage_menu, menu);
	}
	
	 public void showSettingsDialog(int id)
	    {
	        FragmentManager fm = getActivity().getSupportFragmentManager();
	        SettingsDialog md = new SettingsDialog();
	        Bundle args = new Bundle();
	        args.putInt(Arguments.USERID, id);
	        md.setArguments(args);
	        md.show(fm, "dialog_settings_medicine");
	    }
}
