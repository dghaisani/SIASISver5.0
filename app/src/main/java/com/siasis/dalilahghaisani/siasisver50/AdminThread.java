package com.siasis.dalilahghaisani.siasisver50;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siasis.dalilahghaisani.siasisver50.Controller.TabsPagerAdapter;

public class AdminThread extends Fragment implements
		ActionBar.TabListener {

	private ViewPager viewPager;

	private View rootView;
	private TabsPagerAdapter mAdapter;
	// Tab titles
	private String[] tabs = { "Tanya Jawab", "Request Asitensi", "Polling Jadwal" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.activity_admin_forum, container, false);
		getActivity().setContentView(R.layout.activity_admin_forum);

		// Initilization
		viewPager = (ViewPager) rootView.findViewById(R.id.pager);
		final ActionBar actionBar = getActivity().getActionBar();
		mAdapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		assert actionBar != null;
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		return rootView;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

	}
}
