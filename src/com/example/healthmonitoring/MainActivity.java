package com.example.healthmonitoring;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	//private TextView v;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//REKG;TWEW;EKGTWKEG	
		super.onCreate(savedInstanceState);
		//v = new TextView(this);
		setContentView(R.layout.activity_main);
		TextView v = (TextView)findViewById(R.id.helloworld);
		DatabaseHandler db = new DatabaseHandler(this);
		try {
			User jason = new User("jason", "jason", "jason", "jennings", 6, 1, 200, new Date(1987, 8, 15), 'M');
			Log.w("PHMS", jason.getFirstName());
			User schyler = new User("picard", "jason", "schyler", "folefack", 6, 1, 200, new Date(1988, 4, 2), 'M');
			db.store(jason);
			db.store(schyler);
		} catch (UnsupportedEncodingException e) {
				Log.w("PHMS", "Error hashing");
		}
		
		ArrayList<User> users = null;
		try {
			users = db.getUsers();
		} catch (UnsupportedEncodingException e) {
			Log.w("PHMS", "Error hashing");
		}
		Log.w("PHMS", ""+users.size());
		for (User s : users)
		{
			v.append(s.getFirstName() + " " + s.getLastName());
			Log.w("PHMS", s.getFirstName() + " " + s.getLastName());
		}
		// Inflate the menu; this adds items to the action bar if it is present.
// Inflate the menu; this adds items to the action bar if it is present.
		// Inflate the menu; this adds items to the action bar if it is present.
		// Inflate the menu; this adds items to the action bar if it is present.
		// This is a test
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
