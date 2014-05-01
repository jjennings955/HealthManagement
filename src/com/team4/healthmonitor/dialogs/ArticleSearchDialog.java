package com.team4.healthmonitor.dialogs;
import java.util.ArrayList;

import com.team4.database.Article;
import com.team4.database.DatabaseHandler;
import com.team4.healthmonitor.R;
import com.team4.healthmonitor.R.id;
import com.team4.healthmonitor.R.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TimePicker;

@SuppressLint("NewApi")
public class ArticleSearchDialog extends DialogFragment  {
	
	private EditText Search_Data;
	SimpleCursorAdapter mAdapter;
	private DatabaseHandler db;
	public String text_toSearch= null;
	public static ArrayList<Article> arti = null;
	
	
	public ArticleSearchDialog(){}
	
	@Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
 {
		 View view = inflater.inflate(R.layout.dialog_search, container);
		 getDialog().setTitle("Search area");
		 
		
		 db = new DatabaseHandler(getActivity());
			
			
			Search_Data = (EditText)view.findViewById(R.id.search_entry);//serach_entry
		 Button button = (Button)view.findViewById(R.id.search_submitBtn);
	     button.setOnClickListener(new OnClickListener() {
	         public void onClick(View v) {
	             // When button is clicked, call up to owning activity.
	        	 
	        	 text_toSearch = Search_Data.getText().toString();
	        	 
	        	 SQLiteDatabase mdb = db.getWritableDatabase();
	     	     

	     			String query1 = "DELETE FROM article_search";
	     			 mdb.execSQL(query1);
	     	      String query = "INSERT INTO article_search SELECT * FROM article";
	     	      mdb.execSQL(query);
	     	      
	     		//arti = db.getArticle_search(text_toSearch);
	     		sendUpdate();
	        	 dismiss();
	             
	         }
	     });
  
		return view;
		
		
		
  }
	private void sendUpdate() {
		Intent mshg = new Intent("com.team4.healthmonitor.UPDATESEARCH");
		mshg.putExtra("keyword", text_toSearch);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(mshg);
	}

}
