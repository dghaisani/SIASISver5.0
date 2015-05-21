package com.siasis.dalilahghaisani.siasisver50;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.siasis.dalilahghaisani.siasisver50.Controller.DetailPolController;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.ListQuestionAdapter;
import com.siasis.dalilahghaisani.siasisver50.Controller.PollingController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASUS on 5/20/2015.
 */
public class PollingFragment extends Fragment {

    private JSONArray jsonArray;
    ListQuestionAdapter adapt;
    String username;
    private ListView getAllPolling;

    private View rootView;

    private ImageView buttonAdd;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.view_thread_polling, container, false);

        this.username = "rian.fitriansyah";
        getAllPolling = (ListView) rootView.findViewById(R.id.listViewForumPol);

        new GetAllForumPolling().execute(username);

        buttonAdd = (ImageView) rootView.findViewById(R.id.buttonAddForumPol);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollingFragment.this.getActivity(), PollingController.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        getAllPolling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // GEt the customer which was clicked
                    JSONObject reqClicked = jsonArray.getJSONObject(position);
                    int reqp = reqClicked.getInt("Id");
                    int kelasp = reqClicked.getInt("Id_Kelas");

//                        Toast.makeText(getApplicationContext(), "-j=" + jadwalp + "-k=" + kelasp
//                                , Toast.LENGTH_LONG).show();

                    // Send Customer ID
                    Intent showDetails = new Intent(getActivity().getBaseContext(), DetailPolController.class);
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("RequestID", reqp);
                    showDetails.putExtra("KelasID", kelasp);

                    //Toast.makeText(getApplicationContext(), "seharusnya abis ini ke detail", Toast.LENGTH_LONG).show();

                    startActivity(showDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        new GetAllForumPolling().execute(username);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetAllForumPolling extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/polling.php?fun=listfaq&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setListAdapter(jsonArray);
        }
    }

    public  void setListAdapter(JSONArray jsonArray) {
        ListView listForumQuestion = (ListView) rootView.findViewById(R.id.listViewForumPol);
        this.jsonArray = jsonArray;
        adapt = new ListQuestionAdapter(jsonArray, this.getActivity());
        listForumQuestion.setAdapter(adapt);
    }
}
