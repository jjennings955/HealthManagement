package com.team4.healthmonitor;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.team4.database.DatabaseHandler;
import com.team4.database.Helper;

public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 500;
    
	private BroadcastReceiver databaseUpdater = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    String message = intent.getStringExtra("message");
		    Log.d("receiver", "Got message: " + message);
		    Toast foo = Toast.makeText(context, "Finished importing food database", Toast.LENGTH_SHORT);
		    foo.show();
		  }
		};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DatabaseHandler db = new DatabaseHandler(this);
        Log.w("PHMS", Helper.getDate());
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
   }
 
}