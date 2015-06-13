package com.siasis.dalilahghaisani.siasisver50.Controller.ForumPol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.siasis.dalilahghaisani.siasisver50.Controller.ExpandableForumAdapter;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PollingFragment extends Fragment {

    private ExpandableListView GetAllPollingView;

    private View rootView;

    private String username;
    private int role;

    private ImageView buttonAdd;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLE = "role";

    private List<String> listDataHeader;
    private HashMap<String, List<JSONObject>> listDataChild;
    private HashMap<String, String> detailMahasiswa;

    SessionManager session;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.view_thread_polling, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        this.detailMahasiswa = session.getUserDetails();
        this.username = this.detailMahasiswa.get(KEY_NAME);
        this.role = Integer.parseInt(this.detailMahasiswa.get(KEY_ROLE));

        GetAllPollingView = (ExpandableListView) rootView.findViewById(R.id.GetAllPollingListView);

        new GetAllForumPolling(PollingFragment.this).execute(username);

        buttonAdd = (ImageView) rootView.findViewById(R.id.buttonAddForumPol);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollingFragment.this.getActivity(), PollingController.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        GetAllPollingView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                try {
                    // GEt the customer which was clicked
                    int reqp = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getInt("Id");
                    int kelasp = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getInt("Id_Kelas");

                    // Send Customer ID
                    Intent showDetails = new Intent(getActivity().getBaseContext(), DetailPolController.class);
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("RequestID", reqp);
                    showDetails.putExtra("KelasID", kelasp);

                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        new GetAllForumPolling(PollingFragment.this).execute(username);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetAllForumPolling extends AsyncTask<String,Long,JSONArray>
    {
        private ProgressDialog dialog;
        private PollingFragment activity;

        public GetAllForumPolling(PollingFragment activity) {
            this.activity = activity;
            dialog = new ProgressDialog(activity.getActivity());
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            Log.e("user", params[0]);
            String url = "http://ppl-a08.cs.ui.ac.id/polling.php?fun=listfaq&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<JSONObject>>();
            try {
                int i = 0;
                if(jsonArray != null) {
                    while (i < jsonArray.length()) {
                        JSONObject ob = jsonArray.getJSONObject(i);
                        String nama = ob.getString("Nama");

                        List<JSONObject> listChild = new ArrayList<JSONObject>();
                        while (i < jsonArray.length() && nama.equals(jsonArray.getJSONObject(i).getString("Nama"))) {
                            listChild.add(jsonArray.getJSONObject(i));
                            i++;
                        }
                        listDataHeader.add(nama);
                        listDataChild.put(nama, listChild);
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            ExpandableListAdapter listAdapter = new ExpandableForumAdapter(PollingFragment.this.getActivity(), listDataHeader, listDataChild);
            GetAllPollingView.setAdapter(listAdapter);
            for(int j=0; j<listDataHeader.size(); j++){
                GetAllPollingView.expandGroup(j);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
