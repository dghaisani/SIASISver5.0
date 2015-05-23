package com.siasis.dalilahghaisani.siasisver50;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.siasis.dalilahghaisani.siasisver50.Controller.EnrollController;
import com.siasis.dalilahghaisani.siasisver50.Controller.ExpandableEnrollAdapter;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.MenjabatController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 5/20/2015.
 */
public class KelasFragment extends Fragment {

    private String username;

    private ExpandableListView expListView;

    private JSONArray jsonArray;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getActivity());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");

        View view = inflater.inflate(R.layout.list_enroll, container, false);

        //GetAllEnrollListView = (ListView) view.findViewById(R.id.GetAllJadwalListView);
        ImageView enroll = (ImageView) view.findViewById(R.id.button);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        //this.username="rian.fitriansyah";

        new GetAllEnrollTask(KelasFragment.this).execute(username);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //asumsikan kalo dipencet bisa ngeluarin infromasi asistennya
                //viewnya di view_profile_personal
                //kelasnya inten, nah loh pake controller mana nih??
                //asumsikan gue punya kelas ProfileAsdos.java
                String userAsdos = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Intent showDetails = new Intent(getActivity(), MenjabatController.class);
                showDetails.putExtra("Username", userAsdos);
                startActivity(showDetails);
                return false;
            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent showDetails = new Intent(getActivity(), EnrollController.class);
                showDetails.putExtra("Username", username);
                startActivity(showDetails);
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        
        new GetAllEnrollTask(KelasFragment.this).execute(username);
    }

    private class GetAllEnrollTask extends AsyncTask<String,Long,JSONArray>
    {
        private ProgressDialog dialog;
        private KelasFragment activity;

        public GetAllEnrollTask(KelasFragment activity) {
            this.activity = activity;
            dialog = new ProgressDialog(activity.getActivity(), R.style.MyTheme);
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            //kalo sempet hasil keluaran enrollList didikitin
            String url = "http://ppl-a08.cs.ui.ac.id/enroll.php?fun=enrollList&username=" + params[0];
            jsonArray = (jsonParser.getJSONArrayFromUrl(url));
            return jsonArray;
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray != null) {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();
                //String k = "";

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        String kelas = ob.getString("Nama");
                        listDataHeader.add(kelas);
                        List<String> listChild = (new EnrollController()).getAllAsdosKelas(ob.getInt("Id"));
                        listDataChild.put(kelas, listChild);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), k, Toast.LENGTH_LONG).show();
                }

                ExpandableListAdapter listAdapter = new ExpandableEnrollAdapter(KelasFragment.this.getActivity(), listDataHeader, listDataChild, username);
                expListView.setAdapter(listAdapter);
            } if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
