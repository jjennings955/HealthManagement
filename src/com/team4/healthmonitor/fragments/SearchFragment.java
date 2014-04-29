package com.team4.healthmonitor.fragments;


import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.layout;
import com.team4.healthmonitor.R.menu;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFragment extends Fragment 
{

	private FragmentActivity myContext;
	private String username;
	private String password;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		setHasOptionsMenu(true);
		//((MainAppActivity) getActivity()).setActionBarTitle("Search");
		
		//Toast.makeText(getActivity(), myContext+"", Toast.LENGTH_SHORT).show();
		
		
		
		
		
		DatabaseHandler db = new DatabaseHandler(getActivity());
		 //Cursor cursor = getActivity().getContentResolver().q
		 //getActivity().startManagingCursor(cursor);//.query("user", new String[] {"first_name", "last_name"}, null, null, null)
		SQLiteDatabase mdb = db.getWritableDatabase();
		Cursor c = mdb.rawQuery("select id as _id, first_name, last_name from user", null);

         // THE DESIRED COLUMNS TO BE BOUND
         String[] columns = new String[] { "first_name", "last_name" };
         // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
         int[] to = new int[] { R.id.name_entry, R.id.number_entry };

         // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
         SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fragment_search, c, columns, to,0);

         // SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
         //((ListView) rootView).setAdapter(mAdapter);
         //ListFragment l = new ListFragment();
         //ListView l = new ListView(getActivity().getBaseContext());
        setListAdapter(mAdapter);
         //getListView().
		
		
		
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	   inflater.inflate(R.menu.search_menu, menu);
	}
}
