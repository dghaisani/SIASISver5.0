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

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.AdminThread;
import com.siasis.dalilahghaisani.siasisver50.ConfirmProfile;
import com.siasis.dalilahghaisani.siasisver50.ForumPolling;
import com.siasis.dalilahghaisani.siasisver50.ForumQA;
import com.siasis.dalilahghaisani.siasisver50.ForumRequest;
import com.siasis.dalilahghaisani.siasisver50.FragmentOne;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;
import com.siasis.dalilahghaisani.siasisver50.R;
import com.siasis.dalilahghaisani.siasisver50.RequestRole;
import com.siasis.dalilahghaisani.siasisver50.ViewPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PilihanController extends Fragment{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList, getAllRole;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    CustomDrawerAdapter adapter;
    Fragment fragment;

    private int count;
    private int MODE_PRIVATE = 0;

    Button fetch;
    TextView text;
    EditText et;

    List<DrawerItem> dataList;

    private ListView ListRole;
    private JSONArray jsonArray;
    private View rootView;
    Mahasiswa mahasiswa;

    private String username;
    private int role;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLE = "role";

    private ExpandableListView GetAllJadwalListView;
    private View view;

    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<String> listDataHeader2;
    private HashMap<String, List<JSONObject>> listDataChild2;
    private HashMap<String, String> detailMahasiswa;

    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getActivity().getApplicationContext());
        this.detailMahasiswa = session.getUserDetails();
        this.username = this.detailMahasiswa.get(KEY_NAME);
        this.role = Integer.parseInt(this.detailMahasiswa.get(KEY_ROLE));

        View view = inflater.inflate(R.layout.list_jadwal, container, false);
        GetAllJadwalListView = (ExpandableListView) view.findViewById(R.id.GetAllJadwalListView);
        ImageView buat = (ImageView) view.findViewById(R.id.button);
        //Button pilihan = (Button) view.findViewById(R.id.button9);
        if (role == 0)
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /* Called whenever we call invalidateOptionsMenu() */
    /**
     @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
     // If the nav drawer is open, hide action items related to the content view
     boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
     menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
     return super.onPrepareOptionsMenu(menu);
     }
     */

    private void selectItem(int position) {
        // update the main content by replacing fragments
        /*Fragment fragment = new PlanetFragment();*/
        fragment = null;
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                fragment = new Fragment(){
                    @Override
                    public void onResume() {
                        super.onResume();
                        new GetAllJadwalTask(PilihanController.this).execute(username);
                    }

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        View view = inflater.inflate(R.layout.list_jadwal, container, false);
                        GetAllJadwalListView = (ExpandableListView) view.findViewById(R.id.GetAllJadwalListView);
                        ImageView buat = (ImageView) view.findViewById(R.id.button);
                        //Button pilihan = (Button) view.findViewById(R.id.button9);
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
                };
                args.putString(FragmentOne.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                args.putInt("role",2);
                break;
            case 1:
                fragment = new RoleFragment();
                args.putInt("role",1);
                break;
            case 2:
                fragment = new Fragment(){
                    @Override
                    public void onResume() {
                        super.onResume();
                        new GetAllEnrollTask(PilihanController.this).execute(username);
                    }

                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        view = inflater.inflate(R.layout.list_enroll, container, false);

                        //GetAllEnrollListView = (ListView) view.findViewById(R.id.GetAllJadwalListView);
                        ImageView enroll = (ImageView) view.findViewById(R.id.button);
                        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

                        new GetAllEnrollTask(PilihanController.this).execute(username);

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

                };
                args.putInt("role",0);
                break;
            case 3:
                fragment = new Fragment(){
                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState){
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        View view = inflater.inflate(R.layout.choose_forum, container, false);

                        Button buttonForumReply = (Button) view.findViewById(R.id.choose_forum_1);
                        Button buttonForumForum = (Button) view.findViewById(R.id.choose_forum_2);
                        Button buttonForumPolling = (Button) view.findViewById(R.id.choose_forum_3);

                        buttonForumReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ForumQA.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                            }
                        });

                        buttonForumForum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (getActivity(), ForumRequest.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                            }
                        });

                        buttonForumPolling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent (getActivity(), ForumPolling.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                            }
                        });

                        return view;
                    }

                };
                args.putInt("role", 3);
                break;
            case 4:
                //ProfileController profileController2 = new ProfileController(username);
                if(role == 2) {
                    fragment = new Fragment() {

                        @Override
                        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                 Bundle savedInstanceState) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            view = inflater.inflate(R.layout.admin, container, false);

                            //GetAllEnrollListView = (ListView) view.findViewById(R.id.GetAllJadwalListView);
                            Button kelas = (Button) view.findViewById(R.id.button9);
                            Button role = (Button) view.findViewById(R.id.button10);
                            Button database = (Button) view.findViewById(R.id.button11);
                            Button forum = (Button) view.findViewById(R.id.button12);

                            kelas.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent showDetails = new Intent(getActivity(), KelasController.class);
                                    //asumsi username gak null
                                    showDetails.putExtra("Username", username);
                                    showDetails.putExtra("View", "listKelas");
                                    startActivity(showDetails);
                                }
                            });
                            role.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent showDetails = new Intent(getActivity(), RoleController.class);
                                    //asumsi username gak null
                                    showDetails.putExtra("Username", username);
                                    startActivity(showDetails);
                                }
                            });
                            database.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent showDetails = new Intent(getActivity(), Database.class);
                                    startActivity(showDetails);
                                }
                            });

                            forum.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent showDetails = new Intent(getActivity(), AdminThread.class);
                                    startActivity(showDetails);
                                }
                            });
                            return view;
                        }
                    };
                } else {
                    //fragment = new FragmentOne();
                    Toast.makeText(getActivity().getApplicationContext(), "Hei Anda Bukan Admin", Toast.LENGTH_LONG).show();
                }
                args.putInt("role", 4);
                break;
            case 5:
                session.logoutUser();
                getActivity().finish();
                return;
        }
        /*args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);*/
        /*fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        getActivity().setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);*/
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
            dialog = new ProgressDialog(activity.getActivity());
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
                if(jsonArray != null) {
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
            } catch(JSONException e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "lalala masuk ex", Toast.LENGTH_LONG).show();
            }
            ExpandableListAdapter listAdapter = new ExpandableJadwalAdapter(PilihanController.this.getActivity(), listDataHeader2, listDataChild2);
            GetAllJadwalListView.setAdapter(listAdapter);
//            Toast.makeText(getApplicationContext(), "luar for", Toast.LENGTH_LONG).show();
            for(int j=0; j<listDataHeader2.size(); j++){
                //Toast.makeText(getApplicationContext(), "dalem for", Toast.LENGTH_LONG).show();
                GetAllJadwalListView.expandGroup(j);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public  void setListAdapterJadwal(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.GetAllJadwalListView.setAdapter(new ListJadwalAdapter(jsonArray, this.getActivity()));
    }

    private class GetAllEnrollTask extends AsyncTask<String,Long,JSONArray>
    {
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
            } catch(JSONException e){
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
    public class RoleFragment extends Fragment {

        public RoleFragment() {
            // Empty constructor required for fragment subclasses
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.view_profile_personal, container, false);

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
                    MODE_PRIVATE);

            ProfileController profileController= new ProfileController(username);
            mahasiswa = profileController.getMahasiswa(username);

            /*String defaultS = "";
            int defaultI = 0;
            String name = sharedPreferences.getString(getString(R.string.nama_pref), defaultS);
            String np = sharedPreferences.getString(getString(R.string.npm_pref), defaultS);
            String mail = sharedPreferences.getString(getString(R.string.email_pref), defaultS);
            String hp = sharedPreferences.getString(getString(R.string.hp_pref), defaultS);
            String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);
            int stats = sharedPreferences.getInt(getString(R.string.status_pref), defaultI);*/

            if (mahasiswa.getUsername() != null){
                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(mahasiswa.getName());
                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(mahasiswa.getNpm());
                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(mahasiswa.getEmail());
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(mahasiswa.getHp());

                ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText(mahasiswa.getName());
                ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText(mahasiswa.getNpm());
                ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText(mahasiswa.getEmail());
                ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText(mahasiswa.getHp());

                if (mahasiswa.getPath() != null)
                    ((ImageButton) rootView.findViewById(R.id.foto_profil)).setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
                ListRole = (ListView) rootView.findViewById(R.id.list_role);

                new GetAllRole().execute(username);
            }else{
                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText("");
                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText("");
                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText("");
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText("");

                ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText("");
                ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText("");
                ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText("");
                ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText("");
            }


            rootView.findViewById(R.id.foto_profil).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewPicture.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                }
            });

            rootView.findViewById(R.id.buttonProfile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.VISIBLE);
                    ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.VISIBLE);
                    ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.VISIBLE);
                    ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.VISIBLE);
                    ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.INVISIBLE);
                    ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.INVISIBLE);
                    ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.INVISIBLE);
                    ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.INVISIBLE);
                    ((ImageView) rootView.findViewById(R.id.buttonDone)).setVisibility(View.VISIBLE);
                    ((ImageView) rootView.findViewById(R.id.buttonProfile)).setVisibility(View.INVISIBLE);
                }
            });

            rootView.findViewById(R.id.buttonDone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmProfile dialog = ConfirmProfile.newInstance();
                    /*dialog.show(getFragmentManager(), "ConfirmProfile");*/
                }

            });

            rootView.findViewById(R.id.buttonRole).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RequestRole.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }

    private class GetAllRole extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/role.php?fun=listrole&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setRoleAdapter(jsonArray);
        }
    }

    public  void setRoleAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.ListRole.setAdapter(new ListRoleAdapter(jsonArray, this.getActivity()));
    }


    /*private class GetAllEnrollTask extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {
            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/enroll.php?fun=enrollList&username=" + params[0];
            jsonArray = (jsonParser.getJSONArrayFromUrl(url));
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container);
            if(jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setId(i);
                        //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject(i).getString("Nama"), Toast.LENGTH_LONG).show();
                        textView.setText(jsonArray.getJSONObject(i).getString("Nama"));
                        *//*textView.setTextColor(getResources().getColor(R.color.dim_foreground_material_dark));*//*
                        linearLayout.addView(textView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }//Toast.makeText(getApplicationContext(), "Di for", Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/
}
