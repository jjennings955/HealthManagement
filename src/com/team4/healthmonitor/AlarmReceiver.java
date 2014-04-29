package com.team4.healthmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.team4.healthmonitor.R;

public class AlarmReceiver extends BroadcastReceiver 
{

	 @Override
	 public void onReceive(Context arg0, Intent arg1) 
	 {
		 
		 Log.w("PHMS", "Alarm Received: " + arg1.getAction() + " " + arg1.getExtras().getString("MEDNAME")+"");

		 Toast.makeText(arg0, arg1.getExtras().getString("MEDNAME")+"", Toast.LENGTH_LONG).show();
		 
		
		 Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_LONG).show();
	
	 }

}