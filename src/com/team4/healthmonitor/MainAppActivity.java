package com.team4.healthmonitor;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.team4.database.Contact;
import com.team4.database.DatabaseHandler;
import com.team4.database.Helper;
import com.team4.database.MedSchedule;
import com.team4.database.Session;
import com.team4.database.User;


import com.team4.healthmonitor.adapters.TabsPagerAdapter;
import com.team4.healthmonitor.dialogs.EditMedicineDialog;
import com.team4.healthmonitor.dialogs.SecurityDialog;

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

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;
import android.widget.Toast;
import android.os.Vibrator;
@SuppressLint("NewApi")
public class MainAppActivity extends FragmentActivity implements ActionBar.TabListener
{

	public final static String USERNAME = "com.team4.healthmonitor.USERNAME";
	public final static String PASSWORD = "com.team4.healthmonitor.PASSWORD";
	public final static String MEDTIME = "com.team4.healthmonitor.TIME";
	public final static String MEDNAME = "com.team4.healthmonitor.NAME";
	public final static String USERID = "com.team4.healthmonitor.USERID";
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
	private int minsUntilEmail = 2;
	private ArrayList<Integer> medsAlerted = new ArrayList<Integer>();
	public boolean firstTime = true;
	private MediaPlayer player;

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
		    
		    SecurityDialog sd = new SecurityDialog();
		    
		    
		    if((mAccel > 7 && getSupportFragmentManager().findFragmentByTag("SecurityDialog") == null) || (mAccel > 7 && firstTime == true))
		    {
		    	
		        Bundle b = new Bundle();
		        b = getIntent().getExtras();
		        int id = b.getInt(USERID);
		        Bundle args = new Bundle();
		        args.putInt(Arguments.USERID, id);
		        sd.setArguments(args);
		    	sd.show(getSupportFragmentManager(), "SecurityDialog");
		    	sd.setCancelable(false);
		    	firstTime = false;
		    	
		    	
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
		                		Session currentSession = db.currentUserSession(userId);
		                		Timestamp s = new Timestamp(currentSession.getTimestamp());
		                		Calendar session = Calendar.getInstance();
		                		session.setTimeInMillis(s.getTime());
		                		int sessionHour = session.get(Calendar.HOUR);
		                		int sessionMinute = session.get(Calendar.MINUTE);
		                		
		                		ArrayList<Integer> contactIDs = new ArrayList<Integer>();
		                		contactIDs = db.getContactsList(userId);
		                		
		                		String[] toArrTemp = {"",""};
		                		String[] nameTemp = {"",""};
		                		
		                		if(contactIDs.size() == 2)
		                		{
		                			
		                			Contact one = db.getContact(contactIDs.get(0));
		                			Contact two = db.getContact(contactIDs.get(1));
		                			toArrTemp[0] = one.getEmail();
		                			toArrTemp[1] = two.getEmail();
		                			
		                			
		                		}
		                		
		                		nameTemp[0] = currentUser.getFirstName();
		                		nameTemp[1] = currentUser.getLastName();
		                		
		                		
		                		
		                		final String[] toArr = toArrTemp;
		                		final String[] name = nameTemp;
		                		
		                		ArrayList<MedSchedule> scheduleEntries = db.getUserMedicationSchedule(currentUser, Helper.getDay(), Helper.getDate());
		                		
		                		Calendar now = Calendar.getInstance();
		                		now.setTimeInMillis(System.currentTimeMillis());
		                		Calendar medTime = (Calendar) now.clone();
		                		
		                		for(int a = 0; a < scheduleEntries.size(); a++)
		                		{
		                			

			                		String[] medNameTemp = {""};
			                		
			                		medNameTemp[0] = scheduleEntries.get(a).getName(); 
		                		
			                		final String[] medName = medNameTemp;
		                			
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
	                				
	                				if(scheduleEntries.get(a).getPriority() != null && scheduleEntries.get(a).getStatus() == false)
	                				{
		                			
			                			if((medHour > nowHour || (medHour == nowHour && medMin >= nowMin)))
			                			{
			                				
			                				
			                				if(nowHour == medHour && nowMin == medMin)
			                				{
			                					setVolumeControlStream(AudioManager.STREAM_MUSIC);
			                					player = MediaPlayer.create(getBaseContext(), R.raw.notification);
			                					player.start();
			                					Toast.makeText(getApplicationContext(), "You need to take your "+scheduleEntries.get(a).getName()+" now!", Toast.LENGTH_SHORT).show();
			                					Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
			                					v.vibrate(500);
			                					createNotification(scheduleEntries.get(a).getName());
			                				}
			                				
			                				
			                				
			                			}
			                			else if((medHour > sessionHour || (medHour == sessionHour && medMin >= sessionMinute)) && scheduleEntries.get(a).getStatus() == false && scheduleEntries.get(a).getPriority().toString().equalsIgnoreCase("high"))
			                			{
			                				
			                				if(medHour == nowHour && medMin < minsUntilEmail && medMin + minsUntilEmail == nowMin && medsAlerted.contains(scheduleEntries.get(a).getID()) == false)
			                				{
			                					setVolumeControlStream(AudioManager.STREAM_MUSIC);
			                					player = MediaPlayer.create(getBaseContext(), R.raw.email);
			                					player.start();
			                					medsAlerted.add(scheduleEntries.get(a).getID());
			                					Toast.makeText(getApplicationContext(), "SENDING EMAIL!", Toast.LENGTH_SHORT).show();
			                					Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
			                					v.vibrate(500);
			                					Thread thread = new Thread(new Runnable()
			                					{
				                		            @Override
				                		            public void run() {
				                		                try {
				                		                	Mail m = new Mail("personalhealthmonitoringsystem@gmail.com", "admin321");
	
				                		                    m.setTo(toArr);
				                		                    m.setFrom("personalhealthmonitoringsystem@gmail.com");
				                		                    m.setSubject("PHMS - MEDICATION ALERT FOR "+ name[0]+ " " +name[1]);
				                		                    m.setBody(medName[0] + " has not been taken by "+ name[0]+ " " +name[1]+"!");
				                		                    m.send();
				                		                   
				                		                } catch (Exception e) {
				                		                    e.printStackTrace();
				                		                }
				                		            }
				                		            
				                		        });
				                				thread.start();
			                				}
			                				else if(medHour == nowHour && medMin > minsUntilEmail && medsAlerted.contains(scheduleEntries.get(a).getID()) == false)
			                				{
			                					if(medMin + minsUntilEmail >= 60 && medMin + (minsUntilEmail - 60) == nowMin)
			                					{
			                						setVolumeControlStream(AudioManager.STREAM_MUSIC);
				                					player = MediaPlayer.create(getBaseContext(), R.raw.email);
				                					player.start();
			                						medsAlerted.add(scheduleEntries.get(a).getID());
			                						Toast.makeText(getApplicationContext(), "SENDING EMAIL!", Toast.LENGTH_SHORT).show();
				                					Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
				                					v.vibrate(500);
				                					Thread thread = new Thread(new Runnable()
				                					{
					                		            @Override
					                		            public void run() {
					                		                try {
					                		                	Mail m = new Mail("personalhealthmonitoringsystem@gmail.com", "admin321");
		
					                		                	m.setTo(toArr);
					                		                    m.setFrom("personalhealthmonitoringsystem@gmail.com");
					                		                    m.setSubject("PHMS - MEDICATION ALERT FOR "+ name[0]+ " " +name[1]);
						                		                m.setBody(medName[0] + " has not been taken by "+ name[0]+ " " +name[1]+"!");
					                		                    m.send();
					                		                   
					                		                } catch (Exception e) {
					                		                    e.printStackTrace();
					                		                }
					                		            }
					                		            
					                		        });
					                				thread.start();
			                					}
			                					else if(medMin + minsUntilEmail < 60 && medMin + minsUntilEmail == nowMin)
			                					{
			                						setVolumeControlStream(AudioManager.STREAM_MUSIC);
				                					player = MediaPlayer.create(getBaseContext(), R.raw.email);
				                					player.start();
			                						medsAlerted.add(scheduleEntries.get(a).getID());
			                						Toast.makeText(getApplicationContext(), "SENDING EMAIL!", Toast.LENGTH_SHORT).show();
			                						Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
				                					v.vibrate(500);
				                					Thread thread = new Thread(new Runnable()
				                					{
					                		            @Override
					                		            public void run() {
					                		                try {
					                		                	Mail m = new Mail("personalhealthmonitoringsystem@gmail.com", "admin321");
		
					                		                    m.setTo(toArr);
					                		                    m.setFrom("personalhealthmonitoringsystem@gmail.com");
					                		                    m.setSubject("PHMS - MEDICATION ALERT FOR "+ name[0]+ " " +name[1]);
						                		                m.setBody(medName[0] + " has not been taken by "+ name[0]+ " " +name[1]+"!");
					                		                    m.send();
					                		                    
					                		                } catch (Exception e) {
					                		                    e.printStackTrace();
					                		                }
					                		            }
					                		            
					                		        });
					                				thread.start();
			                					}
			              
			                				}
			                				else if(medHour < nowHour && medMin >= (60 - minsUntilEmail) && (medMin + minsUntilEmail) - 60 == nowMin && medsAlerted.contains(scheduleEntries.get(a).getID()) == false)
			                				{
			                					setVolumeControlStream(AudioManager.STREAM_MUSIC);
			                					player = MediaPlayer.create(getBaseContext(), R.raw.email);
			                					player.start();
			                					medsAlerted.add(scheduleEntries.get(a).getID());
			                					Toast.makeText(getApplicationContext(), "SENDING EMAIL!", Toast.LENGTH_SHORT).show();
			                					Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
			                					v.vibrate(500);
			                					Thread thread = new Thread(new Runnable()
			                					{
				                		            @Override
				                		            public void run() {
				                		                try {
				                		                	Mail m = new Mail("personalhealthmonitoringsystem@gmail.com", "admin321");
	
				                		                    m.setTo(toArr);
				                		                    m.setFrom("personalhealthmonitoringsystem@gmail.com");
				                		                    m.setSubject("PHMS - MEDICATION ALERT FOR "+ name[0]+ " " +name[1]);
					                		                m.setBody(medName[0] + " has not been taken by "+ name[0]+ " " +name[1]+"!");
				                		                    m.send();
				                		                    
				                		                } catch (Exception e) {
				                		                    e.printStackTrace();
				                		                }
				                		            }
				                		            
				                		        });
				                				thread.start();
			                				}
			                					
			                				
			                			}
			                				
	                				}
		                		
		                		}
		                    	
		                        
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


