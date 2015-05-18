package com.example.android.navigationdrawerexample.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.navigationdrawerexample.PollingJadwalFragment;
import com.example.android.navigationdrawerexample.RequestAsistensiFragment;
import com.example.android.navigationdrawerexample.TanyaJawabFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new TanyaJawabFragment();
		case 1:
			return new RequestAsistensiFragment();
		case 2:
			return new PollingJadwalFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
