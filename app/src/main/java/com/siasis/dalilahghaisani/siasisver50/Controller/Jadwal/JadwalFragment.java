/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.siasis.dalilahghaisani.siasisver50.Controller.Jadwal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.siasis.dalilahghaisani.siasisver50.Controller.ExpandableJadwalAdapter;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.Profile.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JadwalFragment extends Fragment{
    private String username;
    private int role;

    private ExpandableListView GetAllJadwalListView;

    List<String> listDataHeader2;
    private HashMap<String, List<JSONObject>> listDataChild2;
    private HashMap<String, String> detailMahasiswa;

    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getActivity());
        this.detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");
        this.role = Integer.parseInt(detailMahasiswa.get("role"));

        View view = inflater.inflate(R.layout.list_jadwal, container, false);
        GetAllJadwalListView = (ExpandableListView) view.findViewById(R.id.GetAllJadwalListView);
        ImageView buat = (ImageView) view.findViewById(R.id.button);
        ProfileController m = new ProfileController(username);
        int newRole = m.getRoleMahasiswa();
        if(role != newRole){
            role = newRole;
            session.setRole(newRole);
        }

        Log.e("role", role + "");
        if(role == 0)
            buat.setVisibility(View.GONE);
        else
            buat.setVisibility(View.VISIBLE);

        new GetAllJadwalTask(JadwalFragment.this).execute(username);

        GetAllJadwalListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                GetAllJadwalListView.expandGroup(groupPosition);
                return true;
            }
        });

        GetAllJadwalListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                try {
                    int jadwalp = listDataChild2.get(listDataHeader2.get(groupPosition)).get(childPosition).getInt("Id");
                    int kelasp = listDataChild2.get(listDataHeader2.get(groupPosition)).get(childPosition).getInt("Id_kelas");

                    Intent showDetails = new Intent(getActivity(), JadwalController.class);
                    showDetails.putExtra("JadwalID", jadwalp);
                    showDetails.putExtra("KelasID", kelasp);
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("View", "detailJadwal");
                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        buat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent showDetails = new Intent(getActivity(), JadwalController.class);
                //asumsi username gak null
                showDetails.putExtra("Username", username);
                showDetails.putExtra("View", "createJadwal");

                startActivity(showDetails);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllJadwalTask(JadwalFragment.this).execute(username);
    }

    private class GetAllJadwalTask extends AsyncTask<String,Long,JSONArray> {
        private ProgressDialog dialog;
        private JadwalFragment activity;

        public GetAllJadwalTask(JadwalFragment activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity.getActivity(), R.style.MyTheme);
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/jadwal.php?fun=jadwalList&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            listDataHeader2 = new ArrayList<String>();
            listDataChild2 = new HashMap<String, List<JSONObject>>();
            try {
                int i = 0;
                if (jsonArray != null) {
                    while (i < jsonArray.length()) {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        String tanggal = ob.getString("Tanggal");

                        List<JSONObject> listChild = new ArrayList<JSONObject>();
                        while (i < jsonArray.length() && tanggal.equals(jsonArray.getJSONObject(i).getString("Tanggal"))) {
                            listChild.add(jsonArray.getJSONObject(i));
                            i++;
                        }
                        String[] tgl = tanggal.split("-");
                        tanggal = tgl[2] + "-" + tgl[1] + "-" + tgl[0];
                        listDataHeader2.add(tanggal);
                        listDataChild2.put(tanggal, listChild);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            ExpandableListAdapter listAdapter = new ExpandableJadwalAdapter(JadwalFragment.this.getActivity(), listDataHeader2, listDataChild2);
            GetAllJadwalListView.setAdapter(listAdapter);
            for (int j = 0; j < listDataHeader2.size(); j++) {
                GetAllJadwalListView.expandGroup(j);
            }
        }
    }
}
