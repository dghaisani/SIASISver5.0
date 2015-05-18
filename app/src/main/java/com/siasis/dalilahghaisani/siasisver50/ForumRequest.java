package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.navigationdrawerexample.Controller.DetailReqController;
import com.example.android.navigationdrawerexample.Controller.JSONParser;
import com.example.android.navigationdrawerexample.Controller.ListForumAdapter;
import com.example.android.navigationdrawerexample.Controller.RequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ForumRequest extends Activity {

    private JSONArray jsonArray;
    ListForumAdapter adapt;
    String username;
    private ListView getAllRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_thread_request);

        this.username = getIntent().getStringExtra("Username");
        getAllRequest = (ListView) findViewById(R.id.listViewForumReq);

        new GetAllForumReq().execute(username);

        getAllRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // GEt the customer which was clicked
                    JSONObject reqClicked= jsonArray.getJSONObject(position);
                    int reqp = reqClicked.getInt("Id");
                    int kelasp = reqClicked.getInt("Id_Kelas");

//                        Toast.makeText(getApplicationContext(), "-j=" + jadwalp + "-k=" + kelasp
//                                , Toast.LENGTH_LONG).show();

                    // Send Customer ID
                    Intent showDetails = new Intent (getBaseContext(), DetailReqController.class);
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
    }

    @Override
    protected void onResume(){
        super.onResume();

        new GetAllForumReq().execute(username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forum_request, menu);
        return true;
    }

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

    private class GetAllForumReq extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/request.php?fun=listfaq&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setListAdapter(jsonArray);
        }
    }

    public  void setListAdapter(JSONArray jsonArray) {
        ListView listForumReq = (ListView) findViewById(R.id.listViewForumReq);
        this.jsonArray = jsonArray;
        adapt = new ListForumAdapter(jsonArray, this);
        listForumReq.setAdapter(adapt);
    }

    public void addRequest (View view){
        Intent intent = new Intent(this, RequestController.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }
}
