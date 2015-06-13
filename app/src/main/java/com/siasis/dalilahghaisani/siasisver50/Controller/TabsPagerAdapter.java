package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminPolFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminQAFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminReqFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new AdminQAFragment();
		case 1:
			return new AdminReqFragment();
		case 2:
			return new AdminPolFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
