package com.team4.healthmonitor;


import com.team4.healthmonitor.swipeadapter.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

@SuppressLint("NewApi")
public class MainAppActivity extends FragmentActivity implements ActionBar.TabListener
{

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
		setContentView(R.layout.activity_main_app);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

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

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
	}
	

}
