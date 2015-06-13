package com.siasis.dalilahghaisani.siasisver50.Controller.Admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siasis.dalilahghaisani.siasisver50.Controller.TabsPagerAdapter;
import com.siasis.dalilahghaisani.siasisver50.R;

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
		final ImageView forumQA = (ImageView) rootView.findViewById(R.id.buttonAdminQA);
		final ImageView forumReq = (ImageView) rootView.findViewById(R.id.buttonAdminReq);
		final ImageView forumPol = (ImageView) rootView.findViewById(R.id.buttonAdminPolling);

		Fragment fragment2 = new AdminQAFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment, fragment2);
		fragmentTransaction.commit();


		forumQA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new AdminQAFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();

                forumQA.setImageResource(R.drawable.tanyajawab2);
                forumPol.setImageResource(R.drawable.pol);
                forumReq.setImageResource(R.drawable.req);
			}
		});

		forumReq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new AdminReqFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();

				forumQA.setImageResource(R.drawable.tanyajawab);
				forumPol.setImageResource(R.drawable.pol);
				forumReq.setImageResource(R.drawable.req2);
			}
		});

		forumPol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment2 = new AdminPolFragment();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, fragment2);
				fragmentTransaction.commit();

                forumQA.setImageResource(R.drawable.tanyajawab);
                forumPol.setImageResource(R.drawable.pol2);
                forumReq.setImageResource(R.drawable.req);
			}
		});

		return rootView;
	}

}
