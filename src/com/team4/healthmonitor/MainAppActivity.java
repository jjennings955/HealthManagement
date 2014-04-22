package com.team4.healthmonitor;


import java.util.ArrayList;
import java.util.Calendar;

import com.team4.database.DatabaseHandler;
import com.team4.database.MedSchedule;
import com.team4.database.User;
import com.team4.healthmonitor.swipeadapter.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.os.Build;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		User currentUser = db.getUser(userId);
		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser);
		
		Calendar now = Calendar.getInstance();
		Calendar medTime = (Calendar) now.clone();
		
		for(int a = 0; a < scheduleEntries.size(); a++)
		{
			
			String str = scheduleEntries.get(a).getTime();
			
			medTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(0,1)));
			medTime.set(Calendar.MINUTE, Integer.parseInt(str.substring(str.length() - 2, str.length())));
		    medTime.set(Calendar.SECOND, 0);
			medTime.set(Calendar.MILLISECOND, 0);
	
			//Toast.makeText(getApplicationContext(), now.getTime()+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), medTime.getTime()+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), scheduleEntries.get(2).getTime()+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), Integer.parseInt(str.substring(0,1))+"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), Integer.parseInt(str.substring(str.length() - 2, str.length()))+"", Toast.LENGTH_SHORT).show();
			
			if(medTime.compareTo(now) > 0)
			{
			    //Today Set time had NOT passed
				Toast.makeText(getApplicationContext(), medTime.getTime()+"", Toast.LENGTH_SHORT).show();
				//Toast.makeText(getApplicationContext(), "in", Toast.LENGTH_SHORT).show();
				
				medtime = medTime.getTime()+"";
				medname = scheduleEntries.get(a).getName();
				
				Toast.makeText(getApplicationContext(), medname+"", Toast.LENGTH_SHORT).show();
				
				
				setAlarm(medTime, a+1, medtime, medname);
			}
		
		}
		

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
	

}
