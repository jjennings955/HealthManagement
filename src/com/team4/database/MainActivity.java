package com.team4.database;


import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity  {
/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView v = (TextView)findViewById(R.id.helloworld);
		
		DatabaseHandler db = new DatabaseHandler(this);
		Medication med = new Medication("Tylenol", 1);
		Food f = new Food("Apple", 50.0f, 0.0f, 1.0f, 12.0f, 2.0f, 1.0f);
		db.store(f);
		v.append("Check valid login: " + db.checkLoginInfo("jason", "jason"));
		v.append("Check valid login: " + db.checkLoginInfo("jason", "phason"));
		User jason = new User("jason", "jason", "jason", "jennings", 6, 1, 200, 26, 'M');
		Log.w("PHMS", jason.getFirstName());
		User schyler = new User("schyler", "jason", "schyler", "dsfkjdsfklj", 6, 1, 200, 25, 'M');
		//Log.w("PHMS", "foo " + new Date(schyler.getDate_of_birth().getTime()).toString());
		db.store(jason);
		//db.store(schyler);
		db.store(med);

		ArrayList<User> users = null;
		users = db.getUsers();
		Log.w("PHMS", ""+users.size());
		for (User s : users)
		{
			v.append(s + "\n");
			Log.w("PHMS", s.getFirstName() + " " + s.getLastName());
		}
		ArrayList<Medication> medicines = null;
		medicines = db.getMedications();
		for (Medication m : medicines)
		{
			v.append(m + "\n");
			Log.w("PHMS", m.getName() + " " + m.getPriority());
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
}
