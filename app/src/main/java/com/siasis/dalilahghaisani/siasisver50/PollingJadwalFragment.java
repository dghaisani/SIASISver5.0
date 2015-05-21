package com.siasis.dalilahghaisani.siasisver50;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.siasis.dalilahghaisani.siasisver50.Controller.PollingController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Polling;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PollingJadwalFragment extends Fragment {

	private LinearLayout linearMain;
	private String username;
	private ArrayList<CheckBox> addPolling = new ArrayList<CheckBox>();
	private ArrayList<Polling> pilihan = new ArrayList<Polling>();
	SessionManager session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		session = new SessionManager(getActivity().getApplicationContext());
		HashMap<String, String> detailMahasiswa = session.getUserDetails();
		this.username = detailMahasiswa.get("username");

		View rootView = inflater.inflate(R.layout.fragment_polling_jadwal, container, false);

		linearMain = (LinearLayout) rootView.findViewById(R.id.containerPolling);
		Button adminPolling = (Button) rootView.findViewById(R.id.buttonAdminPolling);

		new GetAllPolling(this).execute(linearMain);

		adminPolling.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String req= "";
				for (int i = 0; i < addPolling.size(); i++) {
					if (addPolling.get(i).isChecked()) {
						req += pilihan.get(addPolling.get(i).getId()).getId() + " ";
						//Toast.makeText(getApplicationContext(), ""+ pilihan.get(addKelas.get(i).getId()).getId(), Toast.LENGTH_LONG).show();
					}
				}
				if (!req.equals("")) {
					req = req.substring(0, req.length() - 1);
					deletePolling(req);
				}
				refresh();
			}
		});


		return rootView;
	}


	private class GetAllPolling extends AsyncTask<LinearLayout,Long,LinearLayout>
	{
		private ProgressDialog dialog;
		private PollingJadwalFragment activity;

		public GetAllPolling(PollingJadwalFragment activity) {
			this.activity = activity;
			dialog = new ProgressDialog(this.activity.getActivity());
		}

		@Override
		protected LinearLayout doInBackground(LinearLayout... params) {
			return params[0];
		}

		protected void onPreExecute() {
			this.dialog.setMessage("Sedang mengambil data...");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(LinearLayout a) {
			ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
			scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

			LinearLayout linearLayout3 = new LinearLayout(getActivity().getApplicationContext());
			linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			linearLayout3.setOrientation(LinearLayout.VERTICAL);

			pilihan = (new PollingController()).getAllPolling();
			if (!pilihan.isEmpty()){
				for (int i = 0; i < pilihan.size(); i++) {
					CheckBox checkBox = new CheckBox(getActivity().getApplicationContext());
					checkBox.setId(i);
					checkBox.setText(pilihan.get(i).getJudul());
					checkBox.setTextColor(getResources().getColor(R.color.black));
					linearLayout3.addView(checkBox);
					addPolling.add(checkBox);
				} scrollView.addView(linearLayout3);
				a.addView(scrollView);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public void deletePolling (final String polling) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("Polling", polling));

		InputStream is = null;

		try {
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/polling.php?fun=deletepoll");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			is = entity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("Client Protocol", "Log_Tag");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Log_Tag", "IOException");
			e.printStackTrace();
		}
	}

	public void refresh(){
		linearMain.removeAllViews();
		new GetAllPolling(this).execute(linearMain);
	}

}
