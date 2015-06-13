package com.siasis.dalilahghaisani.siasisver50.Controller.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.Controller.ForumReq.RequestController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.RequestJadwal;
import com.siasis.dalilahghaisani.siasisver50.R;

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
import java.util.List;

public class AdminReqFragment extends Fragment {

	private LinearLayout linearMain;
	private ArrayList<CheckBox> addRequest = new ArrayList<CheckBox>();
	private ArrayList<RequestJadwal> pilihan = new ArrayList<RequestJadwal>();
	SessionManager session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		View rootView = inflater.inflate(R.layout.fragment_request_asistensi, container, false);

		linearMain = (LinearLayout) rootView.findViewById(R.id.containerReq);
		ImageView adminReq = (ImageView) rootView.findViewById(R.id.buttonAdminReq);

		new GetAllReq(this).execute(linearMain);

		adminReq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String req= "";
				for (int i = 0; i < addRequest.size(); i++) {
					if (addRequest.get(i).isChecked()) {
						req += pilihan.get(addRequest.get(i).getId()).getId() + " ";
						//Toast.makeText(getApplicationContext(), ""+ pilihan.get(addKelas.get(i).getId()).getId(), Toast.LENGTH_LONG).show();
					}
				}
				if (!req.equals("")) {
					req = req.substring(0, req.length() - 1);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminReqFragment.this.getActivity());
					alertDialogBuilder.setTitle("Hapus Thread Question");
					final String finalReq = req;
					alertDialogBuilder.setMessage("Apakah Anda yakin untuk menghapus " + req + " ?").setCancelable(false)
							.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
									StrictMode.setThreadPolicy(policy);

									deleteRequest(finalReq);

									refresh();
									dialog.dismiss();
								}
							})
							.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				}
			}
		});

		
		return rootView;
	}


	private class GetAllReq extends AsyncTask<LinearLayout,Long,LinearLayout>
	{
		private ProgressDialog dialog;
		private AdminReqFragment activity;

		public GetAllReq(AdminReqFragment activity) {
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

            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setPadding(10,0,10,40);
            textView.setText("Silahkan tandai thread request jadwal asistensi yang akan dihapus");
            textView.setTextColor(getResources().getColor(R.color.list_divider));
            linearLayout3.addView(textView);

			pilihan = (new RequestController()).getAllReq();
			if (!pilihan.isEmpty()){
				for (int i = 0; i < pilihan.size(); i++) {
					CheckBox checkBox = new CheckBox(getActivity().getApplicationContext());
					checkBox.setId(i);
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					checkBox.setText(pilihan.get(i).getMateri());
					checkBox.setTextColor(getResources().getColor(R.color.black));
					linearLayout3.addView(checkBox);
					addRequest.add(checkBox);
				} scrollView.addView(linearLayout3);
				a.addView(scrollView);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public void deleteRequest (final String request) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("Request", request));

		InputStream is = null;

		try {
			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/request.php?fun=deletereq");
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
		new GetAllReq(this).execute(linearMain);
	}
}
