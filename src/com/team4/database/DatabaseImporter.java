package com.team4.database;

import com.team4.healthmonitor.Events;
import com.team4.healthmonitor.R;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class DatabaseImporter extends IntentService {

	public DatabaseImporter()
	{
		super("com.team4.database.DatabaseImporter");
		Log.w("PHMS", "DatabaseImporter created!");
	}
	public DatabaseImporter(String name) {
		super(name);
		
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
        DatabaseHandler db = new DatabaseHandler(this);
        Log.w("PHMS", "started importing database");
        DatabaseHandler.importFoodDatabase(db.getWritableDatabase(), this.getResources().openRawResource(R.raw.nutrition), 100);
        db.getWritableDatabase();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Events.DATABASE_IMPORTED));
	}

}
