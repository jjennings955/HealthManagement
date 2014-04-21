package com.team4.healthmonitor.adapters;

import com.team4.healthmonitor.Arguments;
import com.team4.healthmonitor.fragments.DietFragment;
import com.team4.healthmonitor.fragments.MedicineFragment;
import com.team4.healthmonitor.fragments.SearchFragment;
import com.team4.healthmonitor.fragments.StorageFragment;
import com.team4.healthmonitor.fragments.VitalsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter 
{
	private int userId;
	public TabsPagerAdapter(FragmentManager fm, int userId) 
	{
		super(fm);
		this.userId = userId;
	}

	@Override
	public Fragment getItem(int index) 
	{
		Fragment result = null;
		Bundle extras = new Bundle();
		extras.putInt(Arguments.USERID, userId);
		switch (index)
		{
		case 0:
			result = new MedicineFragment();
			break;
		case 1:
			result = new VitalsFragment();
			break;
		case 2:
			result = new StorageFragment();
			break;
		case 3:
			result = new DietFragment();
			break;
		case 4:
			result = new SearchFragment();
			break;
		default:
			result = null;
				
		}
		result.setArguments(extras);
		return result;
	}

	@Override
	public int getCount() 
	{
		// get item count - equal to number of tabs
		return 5;
	}

}

