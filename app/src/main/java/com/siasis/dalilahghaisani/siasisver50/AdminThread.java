package com.siasis.dalilahghaisani.siasisver50;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.siasis.dalilahghaisani.siasisver50.Controller.TabsPagerAdapter;

public class AdminThread extends Fragment {

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

		// Initilization
		Button forumQA = (Button) rootView.findViewById(R.id.buttonAdminQA);
		Button forumReq = (Button) rootView.findViewById(R.id.buttonAdminReq);
		Button forumPol = (Button) rootView.findViewById(R.id.buttonAdminPolling);

		forumQA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new TanyaJawabFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();
			}
		});

		forumReq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new RequestAsistensiFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();
			}
		});

		forumPol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new PollingJadwalFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();
			}
		});

		return rootView;
	}

}
