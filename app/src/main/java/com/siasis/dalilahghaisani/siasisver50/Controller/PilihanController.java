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

package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PilihanController extends Fragment{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    private String username;
    private int role;

    private ExpandableListView GetAllJadwalListView;
    private JSONArray jsonArray;
    private View view;

    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
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
        //Button pilihan = (Button) view.findViewById(R.id.button9);
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

        new GetAllJadwalTask(PilihanController.this).execute(username);

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
                    //Toast.makeText(getApplicationContext(), listDataChild2.get(listDataHeader2.get(groupPosition)).get(childPosition).toString(), Toast.LENGTH_LONG).show();
                    int jadwalp = listDataChild2.get(listDataHeader2.get(groupPosition)).get(childPosition).getInt("Id");
                    int kelasp = listDataChild2.get(listDataHeader2.get(groupPosition)).get(childPosition).getInt("Id_kelas");
////                                    JSONObject mahasiswaClicked = jsonArray.getJSONObject(groupPosition+childPosition);
////                                    int jadwalp = mahasiswaClicked.getInt("Id");
////                                    int kelasp = mahasiswaClicked.getInt("Id_kelas");
                    Intent showDetails = new Intent(getActivity(), JadwalController.class);
                    showDetails.putExtra("JadwalID", jadwalp);
                    showDetails.putExtra("KelasID", kelasp);
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("View", "detailJadwal");
                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "lala masuk ex", Toast.LENGTH_LONG).show();
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
        new GetAllJadwalTask(PilihanController.this).execute(username);
    }

    /*@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActivity().getActionBar().setTitle(mTitle);
    }*/

    private class GetAllJadwalTask extends AsyncTask<String,Long,JSONArray> {
        private ProgressDialog dialog;
        private PilihanController activity;

        public GetAllJadwalTask(PilihanController activity) {
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
                            //Toast.makeText(getApplicationContext(), i + "", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject(i).getString("Tanggal"), Toast.LENGTH_LONG).show();
                            i++;
                        }
                        String[] tgl = tanggal.split("-");
                        tanggal = tgl[2] + "-" + tgl[1] + "-" + tgl[0];
                        listDataHeader2.add(tanggal);
                        listDataChild2.put(tanggal, listChild);
                    }
                }//Toast.makeText(getApplicationContext(), "keluar while luar", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "lalala masuk ex", Toast.LENGTH_LONG).show();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            ExpandableListAdapter listAdapter = new ExpandableJadwalAdapter(PilihanController.this.getActivity(), listDataHeader2, listDataChild2);
            GetAllJadwalListView.setAdapter(listAdapter);
            //            Toast.makeText(getApplicationContext(), "luar for", Toast.LENGTH_LONG).show();
            for (int j = 0; j < listDataHeader2.size(); j++) {
                //Toast.makeText(getApplicationContext(), "dalem for", Toast.LENGTH_LONG).show();
                GetAllJadwalListView.expandGroup(j);
            }
        }

        private class GetAllEnrollTask extends AsyncTask<String, Long, JSONArray> {
            private ProgressDialog dialog;
            private PilihanController activity;

            public GetAllEnrollTask(PilihanController activity) {
                this.activity = activity;
                dialog = new ProgressDialog(activity.getActivity());
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
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();
                //String k = "";

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        String kelas = ob.getString("Nama");
                        //k=kelas;
                        listDataHeader.add(kelas);
                        List<String> listChild = (new EnrollController()).getAllAsdosKelas(ob.getInt("Id"));
                        listDataChild.put(kelas, listChild);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), k, Toast.LENGTH_LONG).show();
                }

                ExpandableListAdapter listAdapter = new ExpandableEnrollAdapter(PilihanController.this.getActivity(), listDataHeader, listDataChild, username);
                expListView.setAdapter(listAdapter);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
//    public class RoleFragment extends Fragment {
//
//        public RoleFragment() {
//            // Empty constructor required for fragment subclasses
//        }
//
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            rootView = inflater.inflate(R.layout.view_profile_personal, container, false);
//
//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
//                    MODE_PRIVATE);
//
//            ProfileController profileController= new ProfileController(username);
//            mahasiswa = profileController.getMahasiswa(username);
//
//            /*String defaultS = "";
//            int defaultI = 0;
//            String name = sharedPreferences.getString(getString(R.string.nama_pref), defaultS);
//            String np = sharedPreferences.getString(getString(R.string.npm_pref), defaultS);
//            String mail = sharedPreferences.getString(getString(R.string.email_pref), defaultS);
//            String hp = sharedPreferences.getString(getString(R.string.hp_pref), defaultS);
//            String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);
//            int stats = sharedPreferences.getInt(getString(R.string.status_pref), defaultI);*/
//
//            if (mahasiswa.getUsername() != null){
//                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(mahasiswa.getName());
//                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(mahasiswa.getNpm());
//                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(mahasiswa.getEmail());
//                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(mahasiswa.getHp());
//
//                ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText(mahasiswa.getName());
//                ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText(mahasiswa.getNpm());
//                ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText(mahasiswa.getEmail());
//                ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText(mahasiswa.getHp());
//
//                if (mahasiswa.getPath() != null)
//                    ((ImageButton) rootView.findViewById(R.id.foto_profil)).setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
//                ListRole = (ListView) rootView.findViewById(R.id.list_role);
//
//                new GetAllRole().execute(username);
//            }else{
//                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText("");
//                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText("");
//                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText("");
//                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText("");
//
//                ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText("");
//                ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText("");
//                ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText("");
//                ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText("");
//            }
//
//
//            rootView.findViewById(R.id.foto_profil).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), ViewPicture.class);
//                    intent.putExtra("Username", username);
//                    startActivity(intent);
//                }
//            });
//
//            rootView.findViewById(R.id.buttonProfile).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.VISIBLE);
//                    ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.VISIBLE);
//                    ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.VISIBLE);
//                    ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.VISIBLE);
//                    ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.INVISIBLE);
//                    ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.INVISIBLE);
//                    ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.INVISIBLE);
//                    ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.INVISIBLE);
//                    ((ImageView) rootView.findViewById(R.id.buttonDone)).setVisibility(View.VISIBLE);
//                    ((ImageView) rootView.findViewById(R.id.buttonProfile)).setVisibility(View.INVISIBLE);
//                }
//            });
//
//            rootView.findViewById(R.id.buttonDone).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ConfirmProfile dialog = ConfirmProfile.newInstance();
//                    /*dialog.show(getFragmentManager(), "ConfirmProfile");*/
//                }
//
//            });
//
//            rootView.findViewById(R.id.buttonRole).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), RequestRole.class);
//                    startActivity(intent);
//                }
//            });
//
//            return rootView;
//        }
//    }
//
//    private class GetAllRole extends AsyncTask<String,Long,JSONArray>
//    {
//        @Override
//        protected JSONArray doInBackground(String... params) {
//
//            // it is executed on Background thread
//            JSONParser jsonParser = new JSONParser();
//            String url = "http://ppl-a08.cs.ui.ac.id/role.php?fun=listrole&username=" + params[0];
//            return (jsonParser.getJSONArrayFromUrl(url));
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray jsonArray) {
//            if(jsonArray != null)
//                setRoleAdapter(jsonArray);
//        }
//    }
//
//    public  void setRoleAdapter(JSONArray jsonArray) {
//        this.jsonArray = jsonArray;
//        this.ListRole.setAdapter(new ListRoleAdapter(jsonArray, this.getActivity()));
//    }
//
//
//    /*private class GetAllEnrollTask extends AsyncTask<String,Long,JSONArray>
//    {
//        @Override
//        protected JSONArray doInBackground(String... params) {
//            // it is executed on Background thread
//            JSONParser jsonParser = new JSONParser();
//            String url = "http://ppl-a08.cs.ui.ac.id/enroll.php?fun=enrollList&username=" + params[0];
//            jsonArray = (jsonParser.getJSONArrayFromUrl(url));
//            return jsonArray;
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray jsonArray) {
//
//            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container);
//            if(jsonArray != null) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
//                        TextView textView = new TextView(getApplicationContext());
//                        textView.setId(i);
//                        //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject(i).getString("Nama"), Toast.LENGTH_LONG).show();
//                        textView.setText(jsonArray.getJSONObject(i).getString("Nama"));
//                        *//*textView.setTextColor(getResources().getColor(R.color.dim_foreground_material_dark));*//*
//                        linearLayout.addView(textView);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }//Toast.makeText(getApplicationContext(), "Di for", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }*/
}
