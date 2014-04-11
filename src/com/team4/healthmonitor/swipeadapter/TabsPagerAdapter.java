package com.team4.healthmonitor.swipeadapter;

import com.team4.healthmonitor.MedicineFragment;
import com.team4.healthmonitor.VitalsFragment;
import com.team4.healthmonitor.StorageFragment;
import com.team4.healthmonitor.DietFragment;
import com.team4.healthmonitor.SearchFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter 
{

	public TabsPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index) 
	{
		switch (index)
		{
		case 0:
			return new MedicineFragment();
		case 1:
			return new VitalsFragment();
		case 2:
			return new StorageFragment();
		case 3:
			return new DietFragment();
		case 4:
			return new SearchFragment();
		}

		return null;
	}

	@Override
	public int getCount() 
	{
		// get item count - equal to number of tabs
		return 5;
	}

}

