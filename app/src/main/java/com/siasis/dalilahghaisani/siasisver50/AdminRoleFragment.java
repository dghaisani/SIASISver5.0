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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.KelasController;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.Model.RequestRole;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 5/20/2015.
 */
public class AdminRoleFragment extends Fragment{

    private LinearLayout linearMain;

    private ArrayList<RequestRole> pilihan = new ArrayList<RequestRole>();

    private View rootView;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rootView = inflater.inflate(R.layout.approve_denny, container, false);
        linearMain = (LinearLayout) rootView.findViewById(R.id.container);

        new GetAllRequestRoleTask(AdminRoleFragment.this).execute(linearMain);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllRequestRoleTask(AdminRoleFragment.this).execute(linearMain);
    }

    public ArrayList<RequestRole> getAll(){
        ArrayList<RequestRole> requestRoles = new ArrayList<RequestRole>();
        String url = "http://ppl-a08.cs.ui.ac.id/requestRole.php?fun=all";
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    requestRoles.add(new RequestRole(jsonObject.getString("Username"), jsonObject.getInt("Id_Kelas"), jsonObject.getInt("Status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return requestRoles;
    }

    public void approve (String username, int idKelas){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Username", username));
        nameValuePairs.add(new BasicNameValuePair("Id_Kelas", Integer.toString(idKelas)));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/approveReqRole.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            String msg = "Berhasil";
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    public void denny (String username, int idKelas){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Username", username));
        nameValuePairs.add(new BasicNameValuePair("Id_Kelas", Integer.toString(idKelas)));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/dennyReqRole.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            String msg = "Berhasil";
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    public String getNamaKelas(ArrayList<Kelas> kelas, int id) {
        for(int i=0; i<kelas.size(); i++) {
            if(kelas.get(i).getId() == id)
                return kelas.get(i).getNama();
        } return null;
    }

    private class GetAllRequestRoleTask extends AsyncTask<LinearLayout,Long,LinearLayout>
    {
        private ProgressDialog dialog;
        private AdminRoleFragment activity;

        public GetAllRequestRoleTask(AdminRoleFragment activity) {
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

            pilihan = getAll();
            ArrayList<Kelas> kelas  = (new KelasController()).getAll();
            if (!pilihan.isEmpty() && !kelas.isEmpty()){
                for (int i = 0; i < pilihan.size(); i++) {
                    LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout linearLayout2 = new LinearLayout(getActivity().getApplicationContext());
                    linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);

                    TextView textView = new TextView(getActivity().getApplicationContext());
                    textView.setId(i);
                    textView.setText(getNamaKelas(kelas,pilihan.get(i).getIdKelas()));
                    textView.setTextColor(getResources().getColor(R.color.black));
                    linearLayout2.addView(textView);

                    TextView textView2 = new TextView(getActivity().getApplicationContext());
                    textView2.setId(i+pilihan.size());
                    textView2.setText(pilihan.get(i).getUsername());
                    textView2.setTextColor(getResources().getColor(R.color.black));
                    linearLayout2.addView(textView2);

                    linearLayout.addView(linearLayout2);

                    if (pilihan.get(i).getStatus() == 0){
                        final Button approve = new Button(getActivity().getApplicationContext());
                        approve.setId(i);
                        approve.setText("Approve");
                        approve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = approve.getId();
                                String username = pilihan.get(pos).getUsername();
                                int idKelas = pilihan.get(pos).getIdKelas();
                                approve(username, idKelas);
                                approve.setVisibility(View.GONE);
                            }
                        });
                        linearLayout.addView(approve);
                    }

                    final Button denny = new Button(getActivity().getApplicationContext());
                    denny.setId(i);
                    denny.setText("Deny");
                    denny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = denny.getId();
                            String username = pilihan.get(pos).getUsername();
                            int idKelas = pilihan.get(pos).getIdKelas();
                            denny(username, idKelas);
                            /*Intent showDetails = new Intent(getActivity().getApplicationContext(), RoleController.class);
                            //asumsi username gak null
                            showDetails.putExtra("Username", username);
                            startActivity(showDetails);
                            finish();*/
                        }
                    });
                    linearLayout.addView(denny);
                    linearLayout3.addView(linearLayout);
                }
                scrollView.addView(linearLayout3);
                a.addView(scrollView);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
