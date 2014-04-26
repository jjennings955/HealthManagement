package com.team4.healthmonitor;



import java.util.ArrayList;
import java.util.Calendar;

import com.team4.database.DatabaseHandler;
import com.team4.database.MedSchedule;
import com.team4.database.User;


import com.team4.healthmonitor.adapters.TabsPagerAdapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainAppActivity extends FragmentActivity implements ActionBar.TabListener
{

	public final static String USERNAME = "com.team4.healthmonitor.USERNAME";
	public final static String PASSWORD = "com.team4.healthmonitor.PASSWORD";
	public final static String MEDTIME = "com.team4.healthmonitor.TIME";
	public final static String MEDNAME = "com.team4.healthmonitor.NAME";
	public static String username = "";
	public static String password = "";

	public static String medname = "";
	public static String medtime="";
	public static int userId;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Medication", "Vitals", "Storage", "Diet", "Search" };
	public Handler mHandler = new Handler();
	private SensorManager mSensorManager;
	  
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	private final SensorEventListener mSensorListener = new SensorEventListener() 
	{

		public void onSensorChanged(SensorEvent se) 
		{
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
		    mAccelLast = mAccelCurrent;
		    mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
		    float delta = mAccelCurrent - mAccelLast;
		    mAccel = mAccel * 0.9f + delta; // perform low-cut filter
		    
		    if(mAccel > 4)
		    {
		    	Log.i("Accelerometer", mAccel+"");
		    }
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) 
		{
		}
	};

	@Override
	protected void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() 
	{
		mSensorManager.unregisterListener(mSensorListener);
    	super.onPause();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		username = i.getStringExtra(MainActivity.USERNAME);
		password = i.getStringExtra(MainActivity.PASSWORD);
		userId = i.getIntExtra(MainActivity.USERID, -1);
/*		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		User currentUser = db.getUser(userId);
		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser);
		
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		Calendar medTime = (Calendar) now.clone();
		
		for(int a = 0; a < scheduleEntries.size(); a++)
		{
		
			
			medTime.set(Calendar.HOUR_OF_DAY, scheduleEntries.get(a).getHour());
			medTime.set(Calendar.MINUTE, scheduleEntries.get(a).getMinutes());
		    medTime.set(Calendar.SECOND, 0);
			medTime.set(Calendar.MILLISECOND, 0);
			
			
			//Toast.makeText(getApplicationContext(), medTime.getTime()+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), scheduleEntries.get(2).getTime()+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), Integer.parseInt(str.substring(0,1))+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), Integer.parseInt(str.substring(str.length() - 2, str.length()))+"", Toast.LENGTH_SHORT).show();
			
			if(medTime.compareTo(now) > 0)
			{
			    //Today Set time had NOT passed
		//		Toast.makeText(getApplicationContext(), "now: "+now.getTime()+"", Toast.LENGTH_SHORT).show();
		//		Toast.makeText(getApplicationContext(), "medTime: "+medTime.getTime()+"", Toast.LENGTH_SHORT).show();
		//		Toast.makeText(getApplicationContext(), scheduleEntries.get(a).getHour()+"   "+scheduleEntries.get(a).getMinutes(), Toast.LENGTH_SHORT).show();
				//	Toast.makeText(getApplicationContext(), "medTime: "+medTime.getTime()+"", Toast.LENGTH_SHORT).show();
				
				//Toast.makeText(getApplicationContext(), "in", Toast.LENGTH_SHORT).show();
				
				medtime = medTime.getTime()+"";
				medname = scheduleEntries.get(a).getName();
				
				//Toast.makeText(getApplicationContext(), medname+"", Toast.LENGTH_SHORT).show();
				
				
				setAlarm(medTime, a+1, medtime, medname);
			}
		
		}*/
		
		new Thread(new Runnable() 
		{
		    @Override
		    public void run() 
		    {
		        // TODO Auto-generated method stub
		        while (true) 
		        {
		            try 
		            {
		                Thread.sleep(10000);
		                mHandler.post(new Runnable() 
		                {
		
		                    

							@Override
		                    public void run() 
		                    {
		                    	
		                    
		                    	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		                		User currentUser = db.getUser(userId);
		                		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser);
		                		
		                		Calendar now = Calendar.getInstance();
		                		now.setTimeInMillis(System.currentTimeMillis());
		                		Calendar medTime = (Calendar) now.clone();
		                		
		                		for(int a = 0; a < scheduleEntries.size(); a++)
		                		{
		                		
		                			
		                			medTime.set(Calendar.HOUR_OF_DAY, scheduleEntries.get(a).getHour());
		                			medTime.set(Calendar.MINUTE, scheduleEntries.get(a).getMinutes());
		                		    medTime.set(Calendar.SECOND, 0);
		                			medTime.set(Calendar.MILLISECOND, 0);
		                			
		                			int nowHour;
	                				int nowMin;
	                				int medHour;
	                				int medMin;
	                				
	                				nowHour = now.get(Calendar.HOUR);
	                				nowMin = now.get(Calendar.MINUTE);
	                				medHour = medTime.get(Calendar.HOUR);
	                				medMin = medTime.get(Calendar.MINUTE);
		                			
		                			if(medHour > nowHour || (medHour == nowHour && medMin >= nowMin))
		                			{
		                			    //Today Set time had NOT passed
		                		//		Toast.makeText(getApplicationContext(), "now: "+now.getTime()+"", Toast.LENGTH_SHORT).show();
		                		//		Toast.makeText(getApplicationContext(), "medTime: "+medTime.getTime()+"", Toast.LENGTH_SHORT).show();
		                		//		Toast.makeText(getApplicationContext(), scheduleEntries.get(a).getHour()+"   "+scheduleEntries.get(a).getMinutes(), Toast.LENGTH_SHORT).show();
		                				//	Toast.makeText(getApplicationContext(), "medTime: "+medTime.getTime()+"", Toast.LENGTH_SHORT).show();
		                				
		                				//Toast.makeText(getApplicationContext(), "in", Toast.LENGTH_SHORT).show();
		                				
		                				
		                				
		                				//Toast.makeText(getApplicationContext(), medname+"", Toast.LENGTH_SHORT).show();
		                				
		                				//Toast.makeText(getApplicationContext(), "now: "+nowHour+":"+nowMin+"   "+scheduleEntries.get(a).getName()+": "+medHour+":"+medMin, Toast.LENGTH_SHORT).show();
		                				
		                				if(nowHour == medHour && nowMin == medMin)
		                				{
		                					Toast.makeText(getApplicationContext(), "Found medicine needed to be taken: "+scheduleEntries.get(a).getName(), Toast.LENGTH_SHORT).show();
		                					createNotification(scheduleEntries.get(a).getName());
		                				}
		                				
		                			}
		                		
		                		}
		                    	
		                        // TODO Auto-generated method stub
		                        // Write your code here to update the UI.
		                    }
		                });
		            } 
		            catch (Exception e) 
		            {
		                // TODO: handle exception
		            }
		        }
		    }
		}).start();
		

/*		for(int a = 0; a < scheduleEntries.size(); a++)
		{
			String str = scheduleEntries.get(a).getTime();
			Toast.makeText(getApplicationContext(), str.substring(str.length() - 2, str.length())+"", Toast.LENGTH_SHORT).show();
		}
*/		
		

	/*	
		Bundle bundle = new Bundle();
		bundle.putString("username", username);
		MedicineFragment fragment=new MedicineFragment();
		fragment.setArguments(bundle);

		
	
		Fragment fragment = new Fragment();
		Bundle bundle = new Bundle();
		bundle.putString(key, username);
		fragment.setArguments(bundle);

*/
		
		
		
		setContentView(R.layout.activity_main_app);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), userId);

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) 
		{
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() 
		{

			@Override
			public void onPageSelected(int position) 
			{
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
				//Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
			}

			@Override
			public void onPageScrollStateChanged(int arg0) 
			{
			}
		});
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		
		for(int i = 0; i < 6; i++)
		{
			if(i == tab.getPosition())
			{
				actionBar.setTitle(tabs[i]);
			}
		}
	}
	public void updateMedicationData()
	{
		
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
	}
	
	private void setAlarm(Calendar targetCal, int requestCode, String medtime, String medname)
	{

		  Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		  intent.putExtra("MEDTIME", medtime);
		  intent.putExtra("MEDNAME", medname);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), requestCode, intent, 0);
		  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		  alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
	}
	
	public void createNotification(String med) 
	{
	    // Prepare intent which is triggered if the
	    // notification is selected
	    Intent intent = new Intent(this, NotificationReceiverActivity.class);
	    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    
	    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

	    // Build notification
	    // Actions are just fake
	    Notification noti = new Notification.Builder(this)
	        .setContentTitle("Take your medication!")
	        .setContentText("You have to take your "+med)
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setLargeIcon(bitmap)
	        .setContentIntent(pIntent).build();
	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    // hide the notification after its selected
	    noti.flags |= Notification.FLAG_AUTO_CANCEL;

	    notificationManager.notify(0, noti);

	  }
	

}


